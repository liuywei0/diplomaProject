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
        <el-table :data="role">
            <el-table-column label="编号" type="index" :index="1" width="100px"></el-table-column>
            <el-table-column label="角色名：" prop="name"></el-table-column>
            <el-table-column label="操作">
                <template v-slot="scope">
                    <el-button type="primary" icon="el-icon-edit" @click="edit(scope.row)" size="small"></el-button>
                    <el-button type="danger" icon="el-icon-delete" @click="del(scope.row.id)" size="small"></el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 对话框 -->
        <el-dialog title="角色信息" :visible.sync="dialogVisible" width="30%">
            <el-form :model="form" label-width="100px" :rules="rules" ref="ruleForm" class="demo-ruleForm">
                <el-form-item label="角色名称：" prop="name">
                    <el-input v-model="form.name" style="width:300px"></el-input>
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
    name: 'Role',
    data() {
        return {
            name: '角色管理',
            dialogVisible: false,
            role: [],
            input: '',
            form: {},
            ruleForm: {
                name: '',
                delivery: false,
            },
            rules: {
                name: [
                    { required: true, message: '请输入角色名', trigger: 'blur' }
                ]
            }
        }
    },
    created() {
        this.loadData();
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
                        url: 'http://localhost:8080/role/saveOrUpdate',
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
                    url: 'http://localhost:8080/role/deleteById',
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
                url: 'http://localhost:8080/role/findAll'
            }).then((response) => {
                if (response.data.code === 200) {
                    this.role = response.data.data;
                } else {
                    alert(response.data.message);
                }
            });
        },
    }
}
</script>

<style>
</style>