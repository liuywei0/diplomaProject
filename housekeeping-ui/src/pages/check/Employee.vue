<template>
    <div class="employee">

        <!-- 表单 -->
        <el-table :data="employee">
            <el-table-column label="编号" type="index" :index="1"></el-table-column>
            <el-table-column label="姓名：" prop="name"></el-table-column>
            <el-table-column label="手机号：" prop="telephone"></el-table-column>
            <el-table-column label="状态：" prop="status"></el-table-column>
            <el-table-column label="性别：" prop="gender"></el-table-column>
            <el-table-column label="身份证号：" prop="idCard"></el-table-column>
            <el-table-column label="银行卡号：" prop="bankCard"></el-table-column>
            <el-table-column label="操作">
                <template v-slot="scope">
                    <!-- <el-button type="primary" icon="el-icon-circle-check" @click="edit(scope.row)"
                        :disabled="scope.row.status == '禁用'"></el-button>
                    <el-button type="danger" icon=" el-icon-circle-close" @click="edit(scope.row)"
                        :disabled="scope.row.status == '正常'"></el-button> -->

                    <el-button type="primary" icon="el-icon-circle-check" @click="check(scope.row)"
                        v-if="scope.row.status == '禁用'" size="small">
                        通过</el-button>
                    <el-button type="danger" icon=" el-icon-circle-close" @click="checkDef(scope.row)"
                        v-if="scope.row.status == '正常'" size="small">
                        禁用</el-button>
                </template>
            </el-table-column>
        </el-table>


    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'User',
    data() {
        return {
            name: '员工审核',
            employee: [],
            form: {}
        }
    },
    created() {
        this.loadData();
    },
    methods: {

        // 加载数据
        loadData() {
            axios({
                method: 'get',
                url: 'http://localhost:8080/user/findAllEmployee'
            }).then((response) => {
                if (response.data.code === 200) {
                    this.employee = response.data.data;

                } else {
                    alert(response.data.message);
                }
            });
        },

        // 审核通过
        check(row) {
            axios({
                method: 'get',
                url: 'http://localhost:8080/user/check',
                params: row
            }).then((response) => {
                if (response.data.code === 200) {
                    //成功弹窗
                    this.$message({
                        message: response.data.message,
                        type: 'success'
                    });

                    // 刷新表单
                    this.loadData();
                } else {
                    alert(response.data.message);
                }
            });
        },

        // 审核不通过，拒绝审核
        checkDef(row) {
            axios({
                method: 'get',
                url: 'http://localhost:8080/user/checkDef',
                params: row
            }).then((response) => {
                if (response.data.code === 200) {
                    //成功弹窗
                    this.$message({
                        message: response.data.message,
                        type: 'success'
                    });

                    // 刷新表单
                    this.loadData();
                } else {
                    alert(response.data.message);
                }
            });
        },
    }
}
</script>
