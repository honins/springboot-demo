package com.hy.demo.model;


import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "user")
public class User {

    @Id
    private Integer id;

    /**
     * 登录账号
     *
     * @mbg.generated
     */
    private String account;

    /**
     * 密码
     *
     * @mbg.generated
     */
    private String password;

    /**
     * 用户名
     *
     * @mbg.generated
     */
    private String name;

    private Integer age;

    private Date create_time;

    private Date update_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }
}