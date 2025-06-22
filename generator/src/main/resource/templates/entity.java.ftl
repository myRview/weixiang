package ${package.Entity};

<#list table.importPackages as pkg>
    import ${pkg};
</#list>
<#if swagger>
    import io.swagger.v3.oas.annotations.media.Schema;
</#if>
<#if entityLombokModel>
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;
</#if>
import com.baomidou.mybatisplus.annotation.TableName;
/**
* <p>
    * ${table.comment!}
    * </p>
*
* @author ${author}
* @since ${date}
*/
<#if entityLombokModel>
    @Data
    @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
</#if>
<#if swagger>
    @Schema(name = "${entity}对象", description = "${table.comment!}")
</#if>
@TableName("${table.name}")
public class ${entity} implements Serializable {

private static final long serialVersionUID = 1L;

<#-- ----------  属性循环  ---------->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>

    <#if field.comment!?length gt 0>
        @Schema(description = "${field.comment}")
    </#if>
    <#if field.keyFlag>
        @TableId(value = "${field.annotationColumnName}", type = IdType.ASSIGN_ID)
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>

<#if !entityLombokModel>
<#-- 生成getter/setter方法 -->
</#if>
}