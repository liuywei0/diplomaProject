<template>
    <div class="login-container">
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on"
            label-position="left">

            <div class="title-container">
                <h3 class="title">登录</h3>
            </div>

            <el-form-item prop="roleId">
                <span class="svg-container">
                    <svg-icon icon-class="dashboard" />
                </span>
                <el-select v-model="loginForm.roleId" placeholder="请选择" style="width:93%">
                    <el-option v-for="r in roles" :key="r.id" :label="r.name" :value="r.id">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item prop="username">
                <span class="svg-container">
                    <svg-icon icon-class="user" />
                </span>
                <el-input ref="username" v-model="loginForm.username" placeholder="请输入用户名" name="username" type="text"
                    tabindex="1" auto-complete="off" />
            </el-form-item>

            <el-form-item prop="password">
                <span class="svg-container">
                    <svg-icon icon-class="password" />
                </span>
                <el-input :key="passwordType" ref="password" v-model="loginForm.password" :type="passwordType"
                    placeholder="请输入密码" name="password" tabindex="2" auto-complete="off"
                    @keyup.enter.native="handleLogin" />
                <span class="show-pwd" @click="showPwd">
                    <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
                </span>
            </el-form-item>

            <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;"
                @click.native.prevent="handleLogin">登录</el-button>
            <div style="text-align:right" class="tips">
                <span style="margin-right:228px;">用户名为您注册时的手机号</span>
                <el-link icon="el-icon-mobile-phone" :underline="false" style="font-size:16px" type="primary"
                    @click.native.prevent="visible = true">注册</el-link>
            </div>

            <el-dialog title="注册" :visible.sync="visible" width="37%">
                <el-form :model="form" label-width="100px" :rules="rules" ref="ruleForm" class="demo-ruleForm">
                    <el-form-item label="用户角色：" prop="roleId">
                        <el-select v-model="form.roleId" placeholder="请选择" style="width:400px">
                            <el-option v-for="r in roles" :key="r.id" :label="r.name" :value="r.id"
                                v-if="r.name != 'admin'">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="姓名：" prop="name">
                        <el-input v-model="form.name" style="width:400px"></el-input>
                    </el-form-item>
                    <el-form-item label="手机号：" prop="telephone">
                        <el-input v-model.number="form.telephone" style="width:400px"></el-input>
                    </el-form-item>
                    <el-form-item label="密码：" prop="password">
                        <el-input v-model="form.password" style="width:400px" show-password></el-input>
                    </el-form-item>
                    <el-form-item label="身份证号：">
                        <el-input v-model="form.idCard" style="width:400px"></el-input>
                    </el-form-item>
                    <el-form-item label=" 银行卡号：">
                        <el-input v-model="form.bankCard" style="width:400px"></el-input>
                    </el-form-item>
                    <el-form-item label=" 性别：">
                        <el-radio-group v-model="form.gender">
                            <el-radio label="男"></el-radio>
                            <el-radio label="女"></el-radio>
                        </el-radio-group>
                    </el-form-item>


                    <div style="text-align:right">
                        <el-button @click="resetForm('ruleForm')">取 消</el-button>
                        <el-button @click="register('ruleForm')">注 册</el-button>
                    </div>
                </el-form>

            </el-dialog>
        </el-form>
    </div>
</template>

<script>
import { validUsername } from '@/utils/validate'
import axios from 'axios';
import { mapState } from 'vuex';

export default {
    name: 'Login',
    computed: {
        ...mapState('user', ['role'])
    },
    data() {
        const validateUsername = (rule, value, callback) => {
            if (value === "") {
                callback(new Error("请输入用户名"))
            } else {
                callback()
            }
        }
        const validatePassword = (rule, value, callback) => {
            if (value === "") {
                callback(new Error('请输入密码'))
            } else {
                callback()
            }
        }
        return {
            loginForm: {
                username: '',
                password: ''
            },
            loginRules: {
                username: [{ required: true, trigger: 'blur', validator: validateUsername }],
                password: [{ required: true, trigger: 'blur', validator: validatePassword }]
            },
            loading: false,
            passwordType: 'password',
            redirect: undefined,
            roles: [],
            visible: false,
            form: {
                gender: "未知",
                status: "禁用"
            },
            ruleForm: {
                name: '',
                roleId: '',
                telephone: '',
                status: '',
                password: '',
                delivery: false,
            },
            rules: {
                name: [
                    { required: true, message: '请输入姓名', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },

                    { min: 3, max: 16, message: '密码长度在3-16个的字符' }
                ],
                telephone: [
                    { required: true, message: '请输入手机号码', trigger: 'blur' },
                    {
                        validator: function (rule, value, callback) {
                            if (/^1[3456789]\d{9}$/.test(value) == false) {
                                callback(new Error("请输入正确的手机号"));
                            } else {
                                callback();
                            }
                        }, trigger: 'blur'
                    },
                ],
                roleId: [
                    { required: true, message: '请选择用户角色', trigger: 'change' }
                ],
                status: [
                    { required: true, message: '请选择用户状态', trigger: 'change' }
                ]
            }
        }
    },

    created() {
        this.loadRoles();
    },

    watch: {
        $route: {
            handler: function (route) {
                this.redirect = route.query && route.query.redirect
            },
            immediate: true
        }
    },
    methods: {
        // 注册
        register(ruleForm) {
            this.$refs[ruleForm].validate((valid) => {
                if (valid) {
                    axios({
                        method: 'post',
                        url: 'http://localhost:8080/user/saveOrUpdate',
                        params: this.form
                    }).then((response) => {
                        if (response.data.code === 200) {
                            //成功弹窗
                            this.$message({
                                message: response.data.message,
                                type: 'success'
                            });

                            // 重置表单规则
                            this.resetForm(ruleForm);
                        } else {
                            this.$message.error(response.data.message);
                        }
                    })

                } else {
                    console.log('error submit!!');
                    return "error";
                }
            });

        },

        // 重置表单
        resetForm(form) {
            //关闭对话框
            this.visible = false;
            // 表单置空
            this.form = {
                gender: "未知",
                status: "禁用"
            };

            this.$refs[form].resetFields();
        },

        showPwd() {
            if (this.passwordType === 'password') {
                this.passwordType = ''
            } else {
                this.passwordType = 'password'
            }
            this.$nextTick(() => {
                this.$refs.password.focus()
            })
        },
        handleLogin() {
            this.$refs.loginForm.validate(valid => {
                if (valid) {
                    this.loading = true
                    this.$store.dispatch('user/login', this.loginForm).then(() => {
                        this.$router.replace('/loading')
                        this.loading = false
                        const timer = setInterval(() => {
                            console.log(this.role, "timer")
                            if (this.role) {
                                if (this.role === 'admin') {
                                    this.$router.replace('/system/user')
                                } else if (this.role === '员工') {
                                    this.$router.replace('/employee/orders')
                                } else if (this.role === '顾客') {
                                    this.$router.replace('/user/shopping')
                                }
                                clearInterval(timer)
                            }
                        }, 200)
                    }).catch(() => {
                        this.loading = false
                    })
                } else {
                    console.log('error submit!!')
                    return false
                }
            })
        },

        loadRoles() {
            axios({
                method: 'get',
                url: 'http://localhost:8080/role/findAll'
            }).then((response) => {
                if (response.data.code === 200) {
                    this.roles = response.data.data;

                } else {
                    alert(response.data.message);
                }
            });
        },


    }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg: #283443;
$light_gray: #fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
    .login-container .el-input input {
        color: $cursor;
    }
}

/* reset element-ui css */
.login-container {
    .el-input {
        display: inline-block;
        height: 47px;
        width: 85%;

        input {
            background: transparent;
            border: 0px;
            -webkit-appearance: none;
            border-radius: 0px;
            padding: 12px 5px 12px 15px;
            color: $light_gray;
            height: 47px;
            caret-color: $cursor;

            &:-webkit-autofill {
                box-shadow: 0 0 0px 1000px $bg inset !important;
                -webkit-text-fill-color: $cursor  !important;
            }
        }
    }

    .el-form-item {
        border: 1px solid rgba(255, 255, 255, 0.1);
        background: rgba(0, 0, 0, 0.1);
        border-radius: 5px;
        color: #454545;
    }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
    min-height: 100%;
    width: 100%;
    background-color: $bg;
    overflow: hidden;

    .login-form {
        position: relative;
        width: 520px;
        max-width: 100%;
        padding: 160px 35px 0;
        margin: 0 auto;
        overflow: hidden;
    }

    .tips {
        font-size: 14px;
        color: #fff;
        margin-bottom: 10px;

        span {
            &:first-of-type {
                margin-right: 16px;
            }
        }
    }

    .svg-container {
        padding: 6px 5px 6px 15px;
        color: $dark_gray;
        vertical-align: middle;
        width: 30px;
        display: inline-block;
    }

    .title-container {
        position: relative;

        .title {
            font-size: 26px;
            color: $light_gray;
            margin: 0px auto 40px auto;
            text-align: center;
            font-weight: bold;
        }
    }

    .show-pwd {
        position: absolute;
        right: 10px;
        top: 7px;
        font-size: 16px;
        color: $dark_gray;
        cursor: pointer;
        user-select: none;
    }
}
</style>
