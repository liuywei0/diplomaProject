<template>
    <div class="address">
        <div class="btns">
            <el-button type="primary" size="small" @click="toAdd()">添加</el-button>
        </div>

        <!-- 表单 -->
        <el-table :data="address">

            <el-table-column label="编号" type="index" :index="1"></el-table-column>
            <el-table-column label="收货人姓名：" prop="username"></el-table-column>
            <el-table-column label="手机号：" prop="telephone"></el-table-column>
            <el-table-column label="省份：" prop="province"></el-table-column>
            <el-table-column label="市：" prop="city"></el-table-column>
            <el-table-column label="区：" prop="area"></el-table-column>
            <el-table-column label="具体地址：" prop="address"></el-table-column>
            <el-table-column label="用户姓名：" prop="user.name"></el-table-column>
            <el-table-column label="操作">
                <template v-slot="scope">
                    <el-button type="primary" icon="el-icon-edit" @click="edit(scope.row)" size="small"></el-button>
                    <el-button type="danger" icon="el-icon-delete" @click="del(scope.row.id)" size="small"></el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 对话框 -->
        <el-dialog title="地址信息" :visible.sync="dialogVisible" width="37%">
            <el-form :model="form" label-width="100px" :rules="rules" ref="ruleForm" class="demo-ruleForm">
                <el-form-item label="用户姓名：" prop="userId">
                    <el-select v-model="form.userId" placeholder="请选择" style="width:400px">
                        <el-option v-for="u in user" :key="u.id" :label="u.name" :value="u.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="收货人姓名：" prop="username">
                    <el-input v-model="form.username" style="width:400px"></el-input>
                </el-form-item>
                <el-form-item label="手机号：" prop="telephone">
                    <el-input v-model="form.telephone" style="width:400px"></el-input>
                </el-form-item>
                <el-form-item label="省份：" prop="province">
                    <el-input v-model="form.province" style="width:400px"></el-input>
                </el-form-item>
                <el-form-item label="市：" prop="city">
                    <el-input v-model="form.city" style="width:400px"></el-input>
                </el-form-item>
                <el-form-item label="区：" prop="area">
                    <el-input v-model="form.area" style="width:400px"></el-input>
                </el-form-item>
                <el-form-item label="具体地址：" prop="address">
                    <el-input v-model="form.address" style="width:400px"></el-input>
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
    name: 'Address',
    data() {
        return {
            name: '地址管理',
            dialogVisible: false,
            address: [],
            user: [],
            form: {},
            ruleForm: {
                username: '',
                telephone: '',
                province: '',
                city: '',
                area: '',
                address: '',
                userId: '',
                delivery: false,
            },
            rules: {
                username: [
                    { required: true, message: '请输入姓名', trigger: 'blur' }
                ],
                telephone: [
                    { required: true, message: '请输入手机号', trigger: 'blur' }
                ],
                province: [
                    { required: true, message: '请输入省份', trigger: 'blur' }
                ],
                city: [
                    { required: true, message: '请输入市', trigger: 'blur' }
                ],
                area: [
                    { required: true, message: '请输入区', trigger: 'blur' }
                ],
                address: [
                    { required: true, message: '请输入具体地址', trigger: 'blur' }
                ],
                userId: [
                    { required: true, message: '请选择所属用户', trigger: 'change' }
                ]
            }
        }
    },
    created() {
        this.loadData();
        this.loadUser();
    },
    methods: {
        toAdd() {
            // 打开对话框
            this.dialogVisible = true;
            // 表单置空
            this.form = {};
        },

        // 关闭对话框
        close(ruleForm) {
            this.dialogVisible = false;
            this.resetForm(ruleForm);
            this.loadData();
        },

        // 保存
        submit(ruleForm) {
            this.$refs[ruleForm].validate((valid) => {
                if (valid) {
                    axios({
                        method: 'post',
                        url: 'http://localhost:8080/address/saveOrUpdate',
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
                    url: 'http://localhost:8080/address/deleteById',
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
            this.form = {};

            this.$refs[form].resetFields();
        },

        loadData() {
            axios({
                method: 'get',
                url: 'http://localhost:8080/address/findAllWithUser'
            }).then((response) => {
                if (response.data.code === 200) {
                    this.address = response.data.data;
                } else {
                    alert(response.data.message);
                }
            });
        },

        loadUser() {
            axios({
                method: 'get',
                url: 'http://localhost:8080/user/findAll'
            }).then((response) => {
                if (response.data.code === 200) {
                    this.user = response.data.data;
                } else {
                    alert(response.data.message);
                }
            });
        }
    }
}
</script>

<style>
.el-form-item__label {
    padding-right: 0px;
}
</style>>
</style>