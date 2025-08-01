package ${package.Entity};

<#list table.importPackages as pkg>
    import ${pkg};
</#list>
<#if swagger>
    import io.swagger.v3.oas.annotations.media.Schema;
    import io.swagger.v3.oas.annotations.media.SchemaProperty;
</#if>
<#if entityLombokModel>
    import lombok.Getter;
    import lombok.Setter;
    import lombok.experimental.Accessors;
</#if>

/**
* <p>
    * ${table.comment!}
    * </p>
*
* @author ${author}
* @since ${date}
*/
<#if entityLombokModel>
    @Getter
    @Setter
    @Accessors(chain = true)
</#if>
<#if table.convert>
    @TableName("${table.name}")
</#if>
<#if swagger>
    @Schema(name = "${entity}", description = "${table.comment!}")
</#if>
public class ${entity} implements Serializable {

private static final long serialVersionUID = 1L;

<#-- ----------  属性遍历  ---------->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>

    <#if field.comment!?length gt 0>
        <#if swagger>
            @Schema(description = "${field.comment}")
        <#else>
            /**
            * ${field.comment}
            */
        </#if>
    </#if>
    <#if field.keyFlag>
    <#-- 主键 -->
        <#if field.keyIdentityFlag>
            @TableId(value = "${field.annotationColumnName}", type = IdType.AUTO)
        <#elseif idType??>
            @TableId(value = "${field.annotationColumnName}", type = IdType.${idType})
        <#elseif field.convert>
            @TableId("${field.annotationColumnName}")
        </#if>
    <#-- 普通字段 -->
    <#elseif field.fill??>
    <#-- 填充字段 -->
        @TableField(value = "${field.annotationColumnName}", fill = FieldFill.${field.fill})
    <#elseif field.convert>
        @TableField("${field.annotationColumnName}")
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
}