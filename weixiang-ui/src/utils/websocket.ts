export default class WebSocketService {
  private socket: WebSocket | null;
  private token: string | null;
  reconnectTimer: ReturnType<typeof setTimeout> | null;
  private eventListeners: Record<string, Array<(data: any) => void>>;
  private baseUrl: string;
  private path: string;

  constructor(baseUrl?: string, path = '/ws/notify') {
    this.socket = null;
    this.token = null;
    this.reconnectTimer = null;
    this.eventListeners = {};
    this.baseUrl = baseUrl || (process.env.VUE_APP_WS_BASE_URL || process.env.VUE_APP_API_BASE_URL || 'http://localhost:8015/api');
    this.path = path;
  }

  // 初始化WebSocket连接
  connect(token: string) {
    // 清除可能存在的重连计时器
    if (this.reconnectTimer) {
      clearTimeout(this.reconnectTimer);
      this.reconnectTimer = null;
    }

    this.token = token;
    
    // 关闭已有连接
    if (this.socket) {
      this.socket.close(1000, '主动重连');
    }

    // 添加页面卸载事件监听，确保关闭浏览器时断开连接
    window.addEventListener('beforeunload', this.handleBeforeUnload);
    
    // 构建连接URL
    const wsProtocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const baseHost = this.baseUrl.replace(/^https?:\/\//, '');
    const url = `${wsProtocol}//${baseHost}${this.path}?token=${encodeURIComponent(token)}`;
    
    try {
      // 创建WebSocket实例，通过协议头传递token（隐藏传递）
      const protocols = [`token=${token}`];
      this.socket = new WebSocket(url);
      
      // 注册事件监听
      this.socket.onopen = (event) => {
        console.log('WebSocket连接已建立');
        this.trigger('open', event);
      };
      
      this.socket.onmessage = (event) => {
        console.log('收到WebSocket消息:', event.data);
        try {
          const message = JSON.parse(event.data);
          // 触发通用message事件
          this.trigger('message', message);
          // 触发特定类型事件
          if (message.type) {
            this.trigger(message.type, message.data);
          }
        } catch (e) {
          console.error('解析WebSocket消息失败', e, '原始消息:', event.data);
          this.trigger('message', event.data);
        }
      };
      
      this.socket.onclose = (event) => {
        console.log(`WebSocket连接关闭，代码: ${event.code}, 原因: ${event.reason}`);
        this.trigger('close', event);
        
        // 非主动关闭则尝试重连（1000是正常关闭码）
        if (event.code !== 1000 && this.token) {
          this.reconnectTimer = setTimeout(() => this.connect(this.token!), 5000);
          console.log('5秒后尝试重连...');
        }
      };
      
      this.socket.onerror = (event) => {
        console.error('WebSocket错误发生:', event);
        this.trigger('error', event);
      };
    } catch (error) {
      console.error('创建WebSocket连接失败:', error);
      this.trigger('error', error);
      if (this.token) {
        this.reconnectTimer = setTimeout(() => this.connect(this.token!), 5000);
      }
    }
  }

  // 关闭连接（带正常关闭码）
  disconnect() {
    if (this.reconnectTimer) {
      clearTimeout(this.reconnectTimer);
      this.reconnectTimer = null;
    }
    if (this.socket) {
      this.socket.close(1000, '主动断开连接');
      this.socket = null;
    }
    // 移除页面卸载监听器
    window.removeEventListener('beforeunload', this.handleBeforeUnload);
  }

  // 处理页面卸载事件的回调
  private handleBeforeUnload = () => {
    this.disconnect();
  }

  // 发送消息到服务器
  sendMessage(message: string | object): boolean {
    if (!this.socket || this.socket.readyState !== WebSocket.OPEN) {
      console.error('WebSocket连接未建立，无法发送消息');
      return false;
    }
    
    try {
      const sendData = typeof message === 'object' ? JSON.stringify(message) : message;
      this.socket.send(sendData);
      return true;
    } catch (error) {
      console.error('发送WebSocket消息失败:', error);
      return false;
    }
  }

  // 注册事件监听器
  on(eventName: string, callback: (data: any) => void) {
    if (!this.eventListeners[eventName]) {
      this.eventListeners[eventName] = [];
    }
    this.eventListeners[eventName].push(callback);
  }

  // 移除事件监听器
  off(eventName: string, callback?: (data: any) => void) {
    if (!this.eventListeners[eventName]) return;
    
    if (callback) {
      this.eventListeners[eventName] = this.eventListeners[eventName].filter(
        cb => cb !== callback
      );
    } else {
      // 如果没有指定回调，则移除所有该事件的监听器
      delete this.eventListeners[eventName];
    }
  }

  // 触发事件
  private trigger(eventName: string, data: any) {
    if (this.eventListeners[eventName]) {
      this.eventListeners[eventName].forEach(callback => {
        try {
          callback(data);
        } catch (error) {
          console.error(`触发事件${eventName}的回调函数出错:`, error);
        }
      });
    }
  }

  // 检查连接状态
  isConnected(): boolean {
    return this.socket !== null && this.socket.readyState === WebSocket.OPEN;
  }
}