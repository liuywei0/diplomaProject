<template>
    <div class="product">
        <div class="btns">
            <el-button type="primary" size="small" @click="toAdd()">添加</el-button>
        </div>

        <el-table :data="products">
            <el-table-column label="编号" type="index" :index="1"></el-table-column>
            <el-table-column label="名称" prop="name"></el-table-column>
            <el-table-column label="单价" prop="price"></el-table-column>
            <el-table-column label="描述" prop="introduce"></el-table-column>
            <el-table-column label="所属分类" prop="category.name"></el-table-column>
            <el-table-column label="操作">
                <template v-slot="scope">
                    <el-button type="primary" icon="el-icon-edit" @click="edit(scope.row)" size="small"></el-button>
                    <el-button type="danger" icon="el-icon-delete" @click="toDelete(scope.row.id)" size="small"></el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog title="商品信息" :visible.sync="dialogVisible" width="35%">
            <el-form :model="form" label-width="100px" :rules="rules" ref="ruleForm" class="demo-ruleForm">
                <el-form-item label="产品名称：" prop="name">
                    <el-input v-model="form.name" style="width:400px"></el-input>
                </el-form-item>
                <el-form-item label="产品单价：" prop="price">
                    <el-input v-model="form.price" style="width:400px"></el-input>
                </el-form-item>
                <el-form-item label="产品描述：">
                    <el-input v-model="form.introduce" type="textarea" style="width:400px"></el-input>
                </el-form-item>
                <el-form-item label="所属栏目：" prop="categoryId">
                    <!-- <el-select v-model="form.categoryId" placeholder="请选择" style="width:400px">
                        <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id">
                        </el-option>
                    </el-select> -->
                    <el-cascader v-model="form.categoryId" :options="categories" placeholder="请选择"
                        :props="{ label: 'name', value: 'id', children: 'categories', checkStrictly: true, emitPath: false }"
                        clearable style="width:400px"></el-cascader>
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
    name: 'Product',
    data() {
        return {
            name: '产品管理',
            dialogVisible: false,
            visible: true,
            products: [],
            categories: [],
            form: {},
            ruleForm: {
                name: '',
                price: '',
                categoryId: '',
                delivery: false,
            },
            rules: {
                name: [
                    { required: true, message: '请输入产品名称', trigger: 'blur' }
                ],
                price: [
                    { required: true, message: '请输入产品价格', trigger: 'blur' }
                ],
                categoryId: [
                    { required: true, message: '请选择所属栏目', trigger: 'change' }
                ]
            }
        }
    },
    created() {
        this.loadData();
        this.loadCategory();
    },

    methods: {
        //删除商品信息
        toDelete(id) {
            this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                //删除
                axios({
                    method: 'get',
                    url: 'http://localhost:8080/product/deleteById',
                    params: {
                        id: id
                    }
                }).then((response) => {
                    if (response.data.code === 200) {
                        this.$message({
                            message: response.data.message,
                            type: 'success'
                        });
                        //刷新页面
                        this.loadData();
                        // 重置表单
                        this.resetForm(this.ruleForm)
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

        //修改商品信息
        edit(row) {
            this.form = row;
            this.dialogVisible = true;
        },

        //去添加，打开添加弹窗
        toAdd() {
            //form表单置空
            this.form = {};
            this.dialogVisible = true;
        },

        //保存商品信息
        submit(ruleForm) {
            this.$refs[ruleForm].validate((valid) => {
                if (valid) {
                    axios({
                        method: 'post',
                        url: 'http://localhost:8080/product/saveOrUpdate',
                        params: this.form
                    }).then((response) => {
                        if (response.data.code === 200) {
                            console.log(this.form)
                            //成功弹窗
                            this.$message({
                                message: response.data.message,
                                type: 'success'
                            });
                            //关闭对话框
                            this.dialogVisible = false;
                            // 清空表单
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

        // 重置表单
        resetForm(formName) {
            this.form = {};
            this.$refs[formName].resetFields();
        },

        // 关闭对话框
        close(ruleForm) {
            this.dialogVisible = false;
            this.resetForm(ruleForm);
            this.loadData();
        },

        //加载商品信息
        loadData() {
            axios({
                method: 'get',
                url: 'http://localhost:8080/product/findAllWithCategory'
            }).then((response) => {
                console.log(response);
                if (response.data.code === 200) {
                    this.products = response.data.data;
                } else {
                    alert(response.data.message);
                }
            });
        },

        // 加载栏目信息
        loadCategory() {
            axios({
                method: 'get',
                url: 'http://localhost:8080/category/findAllWithChildren'
            }).then((response) => {
                this.categories = response.data.data;
                console.log(this.categories);
            })
        }

        // //保存商品信息
        // submit() {
        //     axios({
        //         method: 'post',
        //         url: 'http://localhost:8080/product/saveOrUpdate',
        //         data: this.form,
        //         transformRequest: [function (data) {
        //             return querystring.stringify(data)
        //         }]
        //     }).then((response) => {
        //         if (response.data.code === 200) {
        //             console.log(this.form)
        //             //成功弹窗
        //             this.$message({
        //                 message: response.data.message,
        //                 type: 'success'
        //             });
        //             //关闭对话框
        //             this.dialogVisible = false;
        //         } else {
        //             this.$message.error(response.data.message);
        //         }
        //         //刷新
        //         this.loadData();
        //     })
        // },

    }
}

</script>