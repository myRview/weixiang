package ${package.Controller};

import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
* <p>
    * ${table.comment!} 前端控制器
    * </p>
*
* @author ${author}
* @since ${date}
*/
@Tag(name = "${table.comment!}管理")
@RestController
@RequestMapping("<#if restControllerStyle>/${table.name?replace('sys_','')?replace('_','/')}<#else>/${table.name?replace('sys_','')?replace('_','/')}</#if>")
public class ${table.controllerName} {

@Autowired
private ${table.serviceName} ${table.serviceName?uncap_first};

@PostMapping("/add")
@Operation(summary ="添加${table.comment!}")
public boolean add(@RequestBody ${entity} ${entity?uncap_first}) {
return ${table.serviceName?uncap_first}.save(${entity?uncap_first});
}

@DeleteMapping("/{id}")
@Operation(summary ="删除${table.comment!}")
public boolean delete(@PathVariable Long id) {
return ${table.serviceName?uncap_first}.removeById(id);
}

@PostMapping("/update")
@Operation(summary ="修改${table.comment!}")
public boolean update(@RequestBody ${entity} ${entity?uncap_first}) {
return ${table.serviceName?uncap_first}.updateById(${entity?uncap_first});
}

@GetMapping("/{id}")
@Operation(summary ="查询${table.comment!}详情")
public ${entity} getById(@PathVariable Long id) {
return ${table.serviceName?uncap_first}.getById(id);
}

@GetMapping("/list")
@Operation(summary ="查询${table.comment!}列表")
public List<${entity}> list() {
return ${table.serviceName?uncap_first}.list();
}
}