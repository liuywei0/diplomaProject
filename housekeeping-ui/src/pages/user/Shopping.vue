<template>
  <div class="shopping">

    <div style="text-align:right">
      <span style="margin-right:10px;color: #909399;font-size:13px;">
        账户余额：{{ customer.balance }}
      </span>
      <el-button @click="recharge" plain size="small">充值</el-button>
    </div>

    <el-button type="primary" @click="buy()" plain size="small">加入购物车</el-button>

    <el-table :data="products">
      <el-table-column label="编号" type="index" :index="1" width="120px"></el-table-column>
      <el-table-column label="名称" prop="name"></el-table-column>
      <el-table-column label="单价" prop="price"></el-table-column>
      <el-table-column label="描述" prop="introduce"></el-table-column>
      <el-table-column label="所属分类" prop="category.name"></el-table-column>
      <el-table-column label="加购">
        <template v-slot="scope">
          <el-button type="primary" size="small" @click="open(scope.row)" icon="el-icon-shopping-cart-2" plain>
          </el-button>
          <el-button type="info" size="small" @click="see(scope.row)" icon="el-icon-s-comment" plain></el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="购物信息" :visible.sync="dialogVisible" width="40%">
      <el-form :model="orderLine" label-width="100px">
        <el-form-item label="产品名称：">
          <el-input v-model="orderLine.name" style="width:400px" :readonly="true"></el-input>
        </el-form-item>
        <el-form-item label="产品单价：">
          <el-input v-model="orderLine.price" style="width:400px" :readonly="true"></el-input>
        </el-form-item>
        <el-form-item label="产品描述：">
          <el-input v-model="orderLine.introduce" type="textarea" style="width:400px" :readonly="true"></el-input>
        </el-form-item>
        <el-form-item label="产品数量：">
          <el-input-number v-model="num" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item label="地址：">
          <el-select v-model="form.addressId" placeholder="请选择" style="width:400px">
            <el-option v-for="item in address" :key="item.id" :label="item.address" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="员工：">
          <el-select v-model="form.employeeId" placeholder="请选择您喜爱的员工（不选则随机派单）" style="width:400px" clearable>
            <el-option v-for="item in employee" :key="item.id" :label="item.name" :value="item.id" :disabled="item.sel">
            </el-option>
          </el-select>
          <el-tooltip class="item" effect="light" content="点击查看员工评分" placement="top">
            <el-link type="primary" :underline="false" style="margin-left:10px;" @click="seeEmployee">查看
              <i class="el-icon-view el-icon--right"></i>
            </el-link>
          </el-tooltip>

        </el-form-item>
        <el-form-item align="right">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="toCard()">确 定</el-button>
        </el-form-item>
      </el-form>

    </el-dialog>

    <el-dialog title="评价" :visible.sync="visible" width="35%">
      <el-descriptions v-for="(c, index) in comment" :column="1" :key="index" :colon='false'>
        <el-descriptions-item label="顾客：">{{ c.customer.name }}</el-descriptions-item>
        <el-descriptions-item label="员工：">{{ c.employee.name }}</el-descriptions-item>
        <el-descriptions-item label="评价：">{{ c.content }}</el-descriptions-item>
        <el-descriptions-item label="评分：">
          <el-rate v-model="c.score" disabled show-score text-color="#ff9900">
          </el-rate>
        </el-descriptions-item>
        <el-descriptions-item span="2">
          <el-descriptions v-for="(ch, i) in c.children" :key="i" :column="1" size="small">
            <el-descriptions-item label="追评">{{ ch.content }}</el-descriptions-item>
            <el-descriptions-item label="评分">
              <el-rate v-model="ch.score" disabled show-score text-color="#ff9900" size="small">
              </el-rate>
            </el-descriptions-item>
          </el-descriptions>
        </el-descriptions-item>
      </el-descriptions>
      <div style="text-align:right">
        <el-button @click="visible = false" plain style="margin-right: 0px;">关闭</el-button>
      </div>
    </el-dialog>

    <el-dialog title="员工评分" :visible.sync="epv" width="20%">
      <el-descriptions v-for="(e, index) in employee" :column="1" :key="index" :colon='false'>
        <el-descriptions-item label="员工：">{{ e.name }}</el-descriptions-item>
        <el-descriptions-item label="评分：">
          <el-rate v-model="e.comment" disabled show-score text-color="#ff9900">
          </el-rate>
        </el-descriptions-item>
        <el-descriptions-item>☆———————————————☆</el-descriptions-item>
      </el-descriptions>
      <div style="text-align:right">
        <el-button @click="epv = false" plain style="margin-right: 0px;">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios';
import user from '@/store/modules/user'

export default {
  data() {
    return {
      dialogVisible: false,
      visible: false,
      epv: false,
      products: [],
      category: [],
      customer: [],
      employee: [],
      address: [],
      comment: [],
      orderLines: [],
      orderLine: {},
      form: {},
      num: 1,
      id: '',
      total: 0,
    }
  },

  created() {
    this.loadData();
    this.loadUser();

  },

  methods: {

    reName() {
      // this.total = this.orderLine.price * this.num + this.total;

      this.orderLines.push({
        productId: this.orderLine.id,
        productName: this.orderLine.name,
        productPrice: this.orderLine.price,
        productIntroduce: this.orderLine.introduce,
        number: this.num
      })

      this.form.orderLines = this.orderLines;
      this.form.total = this.total
    },

    recharge() {
      this.$prompt('请输入充值金额', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
      }).then(({ value }) => {
        var money = value;
        axios({
          method: 'get',
          url: 'http://localhost:8080/account/recharge',
          params: {
            userId: this.id,
            money: money
          }
        }).then((response) => {
          if (response.data.code === 200) {
            // 成功弹窗
            this.$message({
              type: 'success',
              message: '成功充值: ' + value + '元'
            });
            this.loadUser();
          } else {
            alert(response.data.message);
          }
        });

      })

    },
    // 打开评论对话框
    see(row) {
      this.loadComment(row.id);
    },

    // 打开购物信息对话框
    open(row) {
      this.loadAddress();
      this.loadEmployee();
      this.orderLine = row;
      this.dialogVisible = true;
    },

    // 查看员工评分
    seeEmployee() {
      this.epv = true;
    },

    // 加入购物车
    toCard() {
      this.reName();
      this.dialogVisible = false;
    },

    // 下单
    buy() {
      var form = this.form;
      if (form.addressId == null) {
        this.$message.error("未添加商品")
      } else {
        axios({
          headers: {
            'Content-Type': 'application/json;charset=utf-8'
          },
          method: 'post',
          dataType: 'json',
          url: 'http://localhost:8080/order/commit',
          data: JSON.stringify(form)
        }).then((response) => {
          if (response.data.code === 200) {

            // 成功弹窗
            this.$message({
              message: response.data.message,
              type: 'success'
            });
          } else {
            alert(response.data.message);
          }
        });
        this.orderLines = [];
        this.total = 0;
        this.num = 1;
      }
    },
    loadData() {
      axios({
        method: 'get',
        url: 'http://localhost:8080/product/findAllWithCategory',
      }).then((response) => {
        if (response.data.code === 200) {

          this.products = response.data.data;
        } else {
          alert(response.data.message);
        }
      });
    },

    loadUser() {
      axios({
        method: 'get',
        url: 'http://localhost:8080/user/findByTel',
        params: { tel: user.state.token }
      }).then((response) => {
        if (response.data.code === 200) {

          this.customer = response.data.data;
          this.id = this.customer.id;
          this.form.customerId = this.customer.id;
        } else {
          alert(response.data.message);
        }
      });
    },

    loadAddress() {
      axios({
        method: 'get',
        url: 'http://localhost:8080/address/findByUserId',
        params: { userId: this.id }
      }).then((response) => {
        if (response.data.code === 200) {

          response.data.data.forEach(address => {
            address.address = address.username + " " + address.telephone + " " + address.province + " " + address.city + " " + address.area + " " + address.address
          });
          this.address = response.data.data;

        } else {
          alert(response.data.message);
        }
      });
    },

    loadEmployee() {
      axios({
        method: 'get',
        url: 'http://localhost:8080/user/findAllEmployee'
      }).then((response) => {
        if (response.data.code === 200) {
          response.data.data.forEach(employee => {
            employee.comment = parseFloat(employee.comment)
            if (employee.status == '正常') {
              employee.sel = false;
            } else {
              employee.sel = true;
              employee.name = employee.name + "（该员工暂停服务）";
            }
          });
          this.employee = response.data.data;

        } else {
          alert(response.data.message);
        }
      });
    },

    // 加载评论
    loadComment(id) {
      axios({
        method: 'get',
        url: 'http://localhost:8080/comment/findByProId',
        params: { productId: id }
      }).then((response) => {
        if (response.data.code === 200) {

          this.comment = response.data.data;
          if (this.comment.length != 0) {
            this.visible = true;
          } else {
            this.$message({
              message: "没有评论",
              type: 'success'
            });
          }

        } else {
          alert(response.data.message);
        }
      });
    }
  }
}
</script>

<style>
</style>