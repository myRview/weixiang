import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { ElNotification, ElMessage } from 'element-plus';

class WebSocketService {
    private stompClient: any = null;
    private baseUrl = "http://localhost:8015"; // 后端地址（生产环境应使用环境变量）
    private isConnected = false;
    private isConnecting = false; // 新增连接中状态
    private reconnectTimer: NodeJS.Timeout | null = null;
    private reconnectDelay = 5000; // 重连延迟时间(ms)
    private maxReconnectAttempts = 10; // 最大重连次数
    private reconnectAttempts = 0; // 当前重连次数
    private eventListeners: Record<string, Function[]> = {};

    /**
     * 建立WebSocket连接
     */
    connect() {
        // 防止重复连接
        if (this.isConnected || this.isConnecting) {
            console.log('[WebSocket] 连接已存在或正在建立，跳过重复连接');
            return;
        }

        // 清除之前的重连定时器
        if (this.reconnectTimer) {
            clearTimeout(this.reconnectTimer);
            this.reconnectTimer = null;
        }

        // 获取token（登录后存储的）
        const token = localStorage.getItem('token');
        if (!token) {
            console.error('[WebSocket] 未获取到token，无法建立连接');
            return;
        }

        this.isConnecting = true;
        console.log('[WebSocket] 开始建立连接...');

        try {
            // 构建完整连接地址（包含后端上下文路径/api）
            const wsUrl = `${this.baseUrl}/api/ws?token=${token}`;
            console.log('[WebSocket] 连接地址:', wsUrl);

            // 创建SockJS连接
            const socket = new SockJS(wsUrl);
            
            // 创建STOMP客户端
            this.stompClient = Stomp.over(socket);
            
            // 生产环境禁用调试日志
            if (process.env.NODE_ENV === 'production') {
                this.stompClient.debug = () => {};
            }

            // 连接服务器
            this.stompClient.connect(
                {},
                (frame: any) => {
                    this.isConnected = true;
                    this.isConnecting = false;
                    this.reconnectAttempts = 0;
                    console.log('[WebSocket] 连接成功:', frame);
                    ElMessage.success('实时通知服务已连接');

                    // 订阅用户专属消息通道
                    this.subscribeToUserNotifications();
                    
                    // 订阅系统公告通道
                    this.subscribeToAnnouncements();
                },
                (error: any) => {
                    this.handleConnectionError(error);
                }
            );
        } catch (error) {
            this.handleConnectionError(error);
        }
    }

    /**
     * 订阅用户通知通道
     */
    private subscribeToUserNotifications() {
        this.stompClient.subscribe('/user/notification', (message: any) => {
            const data = JSON.parse(message.body);
            console.log('[WebSocket] 收到用户消息:', data);
            this.handleMessage(data);
        });
    }

    /**
     * 订阅系统公告通道
     */
    private subscribeToAnnouncements() {
        this.stompClient.subscribe('/topic/announcement', (message: any) => {
            const data = JSON.parse(message.body);
            console.log('[WebSocket] 收到系统公告:', data);
            this.handleAnnouncement(data);
        });
    }

    /**
     * 处理连接错误
     */
    private handleConnectionError(error: any) {
        this.isConnected = false;
        this.isConnecting = false;
        this.reconnectAttempts++;
        
        console.error(`[WebSocket] 连接失败（第${this.reconnectAttempts}次）:`, error);
        
        // 超过最大重连次数
        if (this.reconnectAttempts >= this.maxReconnectAttempts) {
            ElMessage.error('通知服务连接失败，请刷新页面重试');
            return;
        }
        
        // 延迟重连
        ElMessage.warning(`通知服务连接失败，${this.reconnectDelay/1000}秒后重试`);
        this.reconnectTimer = setTimeout(() => this.connect(), this.reconnectDelay);
    }

    /**
     * 断开连接
     */
    disconnect() {
        if (this.reconnectTimer) {
            clearTimeout(this.reconnectTimer);
            this.reconnectTimer = null;
        }
        
        if (this.stompClient && this.isConnected) {
            this.stompClient.disconnect(() => {
                this.isConnected = false;
                console.log('[WebSocket] 连接已断开');
                ElMessage.info('实时通知服务已断开');
            });
        }
    }

    /**
     * 处理用户消息
     */
    private handleMessage(message: any) {
        // 文章审核结果特殊处理
        if (message.messageType === 'ARTICLE_AUDIT') {
            ElNotification({
                title: '文章审核通知',
                message: message.content,
                type: message.status === 'PASS' ? 'success' : 'error',
                duration: 6000
            });
            return;
        }

        // 其他消息类型处理
        ElNotification({
            title: message.title || '新消息',
            message: message.content,
            type: message.type?.toLowerCase() || 'info',
            duration: 5000
        });

        this.emit('message', message);
    }

    /**
     * 处理系统公告
     */
    private handleAnnouncement(data: any) {
        ElNotification({
            title: '系统公告',
            message: data.content,
            type: 'warning',
            duration: 10000,
            customClass: 'system-announcement'
        });
        this.emit('announcement', data);
    }

    /**
     * 发送消息到服务器
     */
    sendMessage(destination: string, data: any) {
        if (!this.isConnected) {
            console.error('[WebSocket] 未连接，无法发送消息');
            ElMessage.warning('通知服务未连接');
            return false;
        }

        try {
            this.stompClient.send(destination, {}, JSON.stringify(data));
            return true;
        } catch (error) {
            console.error('[WebSocket] 发送消息失败:', error);
            ElMessage.error('消息发送失败');
            return false;
        }
    }

    /* 事件监听机制 */
    on(eventName: string, callback: Function) {
        if (!this.eventListeners[eventName]) {
            this.eventListeners[eventName] = [];
        }
        this.eventListeners[eventName].push(callback);
    }
    
    private emit(eventName: string, data: any) {
        this.eventListeners[eventName]?.forEach(cb => cb(data));
    }
    
    off(eventName: string, callback?: Function) {
        if (!callback) {
            delete this.eventListeners[eventName];
        } else {
            this.eventListeners[eventName] = this.eventListeners[eventName]?.filter(cb => cb !== callback);
        }
    }

    /**
     * 获取当前连接状态
     */
    getConnectionStatus() {
        return {
            isConnected: this.isConnected,
            isConnecting: this.isConnecting,
            reconnectAttempts: this.reconnectAttempts
        };
    }
}

// 导出单例实例
export const webSocketService = new WebSocketService();