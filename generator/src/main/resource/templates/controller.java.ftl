package ${package.Controller};

<#import "common.ftl" as common>
<#assign restControllerStyle = true>
import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;

<#if restControllerStyle>
    @RestController
<#else>
    @Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
@Tag(name = "${entity}控制器", description = "${table.comment!}接口")
public class ${table.controllerName} {

@Resource
private ${table.serviceName} ${table.serviceName?uncap_first};

@Operation(summary = "新增${entity}")
@PostMapping
public Boolean save(@RequestBody ${entity} entity) {
return ${table.serviceName?uncap_first}.save(entity);
}

@Operation(summary = "删除${entity}")
@DeleteMapping("/{id}")
public Boolean delete(@PathVariable Long id) {
return ${table.serviceName?uncap_first}.removeById(id);
}

@Operation(summary = "更新${entity}")
@PutMapping
public Boolean update(@RequestBody ${entity} entity) {
return ${table.serviceName?uncap_first}.updateById(entity);
}

@Operation(summary = "查询${entity}详情")
@GetMapping("/{id}")
public ${entity} get(@PathVariable Long id) {
return ${table.serviceName?uncap_first}.getById(id);
}

@Operation(summary = "分页查询${entity}")
@GetMapping("/page")
public IPage<${entity}> page(@RequestParam Integer pageNum,
@RequestParam Integer pageSize) {
return ${table.serviceName?uncap_first}.page(new Page<>(pageNum, pageSize));
}

@Operation(summary = "查询${entity}列表")
@GetMapping("/list")
public List<${entity}> list() {
return ${table.serviceName?uncap_first}.list();
}
}