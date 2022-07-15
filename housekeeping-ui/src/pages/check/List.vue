<template>
    <div class="list">

        <!-- 表单 -->
        <el-table :data="account">
            <el-table-column label="编号" type="index" :index="1"></el-table-column>
            <el-table-column label="姓名" prop="user.name"></el-table-column>
            <el-table-column label="时间" prop="transactionTime" sortable></el-table-column>
            <el-table-column label="类型" prop="type" sortable></el-table-column>
            <el-table-column label="描述" prop="description"></el-table-column>
            <el-table-column label="金额" prop="money"></el-table-column>
        </el-table>


    </div>
</template>

<script>
import axios from 'axios';
import { getDate } from '@/utils/common'

export default {
    name: 'list',
    data() {
        return {
            account: [],
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
                url: 'http://localhost:8080/account/findAll'
            }).then((response) => {
                if (response.data.code === 200) {
                    response.data.data.forEach(account => {
                        account.transactionTime = getDate(account.transactionTime, 'yyyy年MM月dd日 hh:mm:ss');
                    });
                    this.account = response.data.data;

                } else {
                    alert(response.data.message);
                }
            });
        },


    }
}
</script>
