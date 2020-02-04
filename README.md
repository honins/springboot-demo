# Springboot-demo
springboot模板项目，整合了以下
- jwt
- redis
- apache shiro
使用token验证身份，利用redis缓存，可在线刷新token。

### 使用tk-mybatis
为持久化层mybatis的开发，demo中使用了tk-mybatis，引入tk-mybatis后，mapper直接继承tk-mybatis的Mapper接口，无需写xml文件即可实现很全面的单表操作，支持使用example对象方式查询。
我在看github上的wiki使用tk-mybatis时出现了以下几个问题：
1. 直接继承Mapper接口即可，继承BaseMapper没有Example对象方式查询
2. model层需要加上一个注解 @Table(name="xxx")，否则会报错：“XXX实体没有指定表名”
3. mapper继承tk-mybatis的Mapper接口需指定具体的实体类型，如：`public interface UserMapper extends Mapper<User>`,不能直接用泛型，否则会报错泛型不能转换成类。
4. MapperScan注解需使用类为`tk.mybatis.spring.annotation.MapperScan`，而不是`org.springframework.context.annotation.MapperScan`

