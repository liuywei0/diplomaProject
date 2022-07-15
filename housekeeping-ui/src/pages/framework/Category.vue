<template>
    <div class="category">
        <!-- 添加按钮 -->
        <div class="btns">
            <el-button type="primary" size="small" @click="toAdd()">添加</el-button>
        </div>

        <!-- 表单 -->
        <el-table :data="categories" style="width: 100%;margin-bottom: 20px;" row-key="id" default-expand-all
            :tree-props="{ children: 'categories', hasChildren: 'hasChildren' }">
            <el-table-column type="index" :index="1" label="编号" sortable width="200">
            </el-table-column>
            <el-table-column prop="name" label="栏目" sortable>
            </el-table-column>
            <el-table-column prop="num" label="序号" sortable>
            </el-table-column>
            <el-table-column label="操作">
                <template v-slot="scope">
                    <el-button type="primary" icon="el-icon-edit" @click="edit(scope.row)" size="small"></el-button>
                    <el-button type="danger" icon="el-icon-delete" @click="del(scope.row.id)" size="small"></el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 对话框 -->
        <el-dialog title="栏目信息" :visible.sync="dialogVisible" width="35%">
            <el-form :model="form" label-width="100px" :rules="rules" ref="ruleForm" class="demo-ruleForm">
                <el-form-item label="栏目名称：" prop="name">
                    <el-input v-model="form.name" style="width:400px"></el-input>
                </el-form-item>
                <el-form-item label="栏目序号：">
                    <el-input v-model="form.num" style="width:400px"></el-input>
                </el-form-item>
                <el-form-item label="所属栏目：">
                    <el-select v-model="form.parentId" placeholder="请选择" style="width:400px">
                        <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item align="right">
                    <el-button @click="close('ruleForm')">取 消</el-button>
                    <el-button @click="resetForm('ruleForm')">重 置</el-button>
                    <el-button type="primary" @click="submit('ruleForm')">确 定</el-button>
                </el-form-item>
            </el-form>

            <!-- <span slot="footer" class="dialog-footer">
                
            </span> -->
        </el-dialog>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            name: '栏目管理',
            dialogVisible: false,
            categories: [],
            form: {},
            ruleForm: {
                name: '',
                num: '',
                delivery: false,
            },
            rules: {
                name: [
                    { required: true, message: '请输入活动名称', trigger: 'blur' }
                ]
            }
        }

    },
    created() {
        this.loadData();
    },

    methods: {
        // 添加信息，打开对话框
        toAdd() {
            // 清空表单
            this.form = {};
            this.dialogVisible = true;
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
                        url: 'http://localhost:8080/category/saveOrUpdate',
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
                    url: 'http://localhost:8080/category/deleteById',
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
        resetForm(formName) {
            this.form = {};
            this.$refs[formName].resetFields();
        },

        // 加载数据
        loadData() {
            axios({
                method: 'get',
                url: 'http://localhost:8080/category/findAllWithChildren'
            }).then((response) => {
                this.categories = response.data.data;
                console.log(this.categories);
            })
        },

    }
}
</script>