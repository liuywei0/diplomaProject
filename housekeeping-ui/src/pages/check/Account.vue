<template>
    <div class="account">

        <!-- 表单 -->
        <el-table :data="account">
            <el-table-column label="编号" type="index" :index="1"></el-table-column>
            <el-table-column label="姓名" prop="user.name"></el-table-column>
            <el-table-column label="类型" prop="type"></el-table-column>
            <el-table-column label="理由" prop="description"></el-table-column>
            <el-table-column label="余额" prop="user.balance"></el-table-column>
            <el-table-column label="提现金额" prop="money"></el-table-column>
            <el-table-column label="操作">
                <template v-slot="scope">
                    <el-button type="primary" icon="el-icon-circle-check" @click="checkAgree(scope.row)" size="small">
                        同意</el-button>
                    <el-button type="danger" icon=" el-icon-circle-close" @click="checkDef(scope.row)" size="small">
                        拒绝</el-button>
                </template>
            </el-table-column>
        </el-table>


    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'account',
    data() {
        return {
            name: '提现审核',
            msg: '',
            account: [],
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
                url: 'http://localhost:8080/account/selectWithdrawal'
            }).then((response) => {
                if (response.data.code === 200) {
                    this.account = response.data.data;

                } else {
                    alert(response.data.message);
                }
            });
        },

        // 审核通过
        checkAgree(row) {
            this.msg = "同意";
            this.check(row);
        },

        // 审核不通过，拒绝审核
        checkDef(row) {
            this.msg = "拒绝";
            this.check(row);
        },
        check(row) {
            // console.log(row)
            // let msg: string = this.msg;
            let msg = this.msg
            axios({
                method: 'get',
                url: 'http://localhost:8080/account/checkWithdrawal',
                params: { id: row.id, userId: row.userId, msg }
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
        }
    }
}
</script>
