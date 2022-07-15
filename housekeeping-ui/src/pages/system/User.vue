<template>
    <div class="user">
        <div class="btns">
            <el-button type="primary" size="small" @click="toAdd()">添加</el-button>
            <!-- <el-input placeholder="请输入内容" v-model="input" style="width:400px; margin-left: 100px;" size="small">
                <i class="el-input__icon"></i>
                <el-button type="primary" size="small" icon="el-icon-search" slot="append"></el-button>
            </el-input> -->
            
        </div>

        <!-- 表单 -->
        <el-table :data="user">
            <el-table-column label="编号" type="index" :index="1"></el-table-column>
            <el-table-column label="用户名：" prop="name"></el-table-column>
            <el-table-column label="手机号：" prop="telephone"></el-table-column>
            <el-table-column label="状态：" prop="status"></el-table-column>
            <el-table-column label="性别：" prop="gender"></el-table-column>
            <el-table-column label="身份证号：" prop="idCard"></el-table-column>
            <el-table-column label="银行卡号：" prop="bankCard"></el-table-column>
            <el-table-column label="角色：" prop="role.name"></el-table-column>
            <el-table-column label="操作">
                <template v-slot="scope">
                    <el-button type="primary" icon="el-icon-edit" @click="edit(scope.row)" size="small"></el-button>
                    <el-button type="danger" icon="el-icon-delete" @click="del(scope.row.id)" size="small"></el-button>
                </template>
            </el-table-column>
        </el-table>

        <div class="block">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :page-sizes="[5, 10, 15, 20]" :page-size="10" layout="total, sizes, prev, pager, next, jumper"
                :total="user.length">
            </el-pagination>
        </div>

        <!-- 对话框 -->
        <el-dialog title="用户信息" :visible.sync="dialogVisible" width="35%">
            <el-form :model="form" label-width="100px" :rules="rules" ref="ruleForm" class="demo-ruleForm">
                <el-form-item label="用户角色：" prop="roleId">
                    <el-select v-model="form.roleId" placeholder="请选择" style="width:400px">
                        <el-option v-for="r in role" :key="r.id" :label="r.name" :value="r.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="用户名：" prop="name">
                    <el-input v-model="form.name" style="width:400px"></el-input>
                </el-form-item>
                <el-form-item label="密码：" prop="password">
                    <el-input v-model="form.password" style="width:400px" show-password></el-input>
                </el-form-item>
                <el-form-item label="手机号：" prop="telephone">
                    <el-input v-model.number="form.telephone" style="width:400px"></el-input>
                </el-form-item>
                <el-form-item label="身份证号：">
                    <el-input v-model="form.idCard" style="width:400px"></el-input>
                </el-form-item>
                <el-form-item label=" 银行卡号：">
                    <el-input v-model="form.bankCard" style="width:400px"></el-input>
                </el-form-item>
                <el-form-item label="状态：" prop="status">
                    <el-radio-group v-model="form.status">
                        <el-radio label="正常"></el-radio>
                        <el-radio label="禁用"></el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label=" 性别：">
                    <el-radio-group v-model="form.gender">
                        <el-radio label="男"></el-radio>
                        <el-radio label="女"></el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item align="right">
                    <el-button @click="close('ruleForm')">取 消</el-button>
                    <el-button @click="resetForm('ruleForm')">重 置</el-button>
                    <el-button type="primary" @click="submit('ruleForm')">确 定</el-button>
                </el-form-item>
            </el-form>

        </el-dialog>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'User',
    data() {
        return {
            name: '用户管理',
            dialogVisible: false,
            user: [],
            role: [],
            input:'',
            form: {
                gender: "未知",
                status: "正常"
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
        this.loadData();
        this.loadRole();
    },
    methods: {

        handleSizeChange(val) {
            console.log(`每页 ${val} 条`);
        },
        handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
        },

        toAdd() {
            // 打开对话框
            this.dialogVisible = true;
            // 表单置空
            this.form = {
                gender: "未知",
                status: "正常"
            };
        },

        // 关闭对话框
        close(ruleForm) {
            this.dialogVisible = false;
            this.resetForm(ruleForm);
            this.loadData();
        },

        // 编辑
        edit(row) {
            // 将form表单填入信息
            this.form = row;
            // 打开对话框
            this.dialogVisible = true;
        },

        // 删除
        del(id) {
            this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                axios({
                    method: 'get',
                    url: 'http://localhost:8080/user/deleteById',
                    params: { id: id }
                }).then((response) => {
                    if (response.data.code === 200) {
                        //成功弹窗
                        this.$message({
                            message: response.data.message,
                            type: 'success'
                        });
                        //刷新
                        this.loadData();
                    } else {
                        this.$message.error(response.data.message);
                    }
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },

        // 重置表单
        resetForm(form) {
            // 表单置空
            this.form = {
                gender: "未知",
                status: "正常"
            };

            this.$refs[form].resetFields();
        },

        // 保存
        submit(ruleForm) {
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
                            //关闭对话框
                            this.dialogVisible = false;
                            // 重置表单规则
                            this.resetForm(ruleForm);
                            //刷新
                            this.loadData();
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

        // 加载用户信息
        loadData() {
            axios({
                method: 'get',
                url: 'http://localhost:8080/user/findAllWithRole'
            }).then((response) => {
                if (response.data.code === 200) {
                    // 将拿到的用户信息放在user中
                    this.user = response.data.data;
                } else {
                    // 若请求失败，则会弹出失败原因弹窗
                    alert(response.data.message);
                }
            });
        },

        loadRole() {
            axios({
                method: 'get',
                url: 'http://localhost:8080/role/findAll'
            }).then((response) => {
                if (response.data.code === 200) {
                    this.role = response.data.data;
                } else {
                    alert(response.data.message);
                }
            });
        }
    }
}
</script>
