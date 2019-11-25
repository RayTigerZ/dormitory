<template>
  <div>
    <div class="toolbar">
      <el-form inline :model="search">
        <el-form-item>
          <el-input
            v-model="search.account"
            placeholder="操作者帐号"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-date-picker
            v-model="search.begin"
            type="date"
            placeholder="开始时间"
            format="yyyy-MM-dd"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-date-picker
            v-model="search.end"
            type="date"
            placeholder="结束时间"
            format="yyyy-MM-dd"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="getPage()"
            >查询</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <el-table
      class="loading-area"
      :data="logs"
      width="100%"
      stripe
      border
      highlight-current-row
      :header-cell-style="headerStyle"
    >
      <el-table-column type="index" width="100" align="center">
      </el-table-column>
      <el-table-column prop="account" label="账户" width="130" align="center">
      </el-table-column>
      <el-table-column
        prop="requestUri"
        label="操作地址"
        width="150"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="params"
        label="参数"
        min-width="360"
        align="center"
        show-overflow-tooltip=""
      >
      </el-table-column>
      <el-table-column
        prop="remoteAddr"
        label="IP地址"
        width="140"
        align="center"
      >
      </el-table-column>
      <el-table-column prop="httpMethod" label="方法" width="90" align="center"
        ><template slot-scope="scope">
          <el-tag size="medium" :type="tagType(scope.row.httpMethod)">{{
            scope.row.httpMethod
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="操作时间"
        min-width="150"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="useTime"
        label="请求用时"
        width="110"
        align="center"
      >
      </el-table-column>
    </el-table>
    <el-pagination
      hide-on-single-page
      :current-page.sync="search.pageNum"
      :page-sizes="[10, 20, 50, 100]"
      :page-size.sync="search.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>
  </div>
</template>

<script>
import { getRequest } from "@/utils/request";

export default {
  data() {
    return {
      logs: [],
      search: {
        pageNum: 1,
        pageSize: 10,
        account: "",
        begin: "",
        end: ""
      },
      total: 0,
      headerStyle: { "background-color": "#c6c6c6", color: "#42464e" },
      tableLoading: false
    };
  },
  methods: {
    getPage() {
      this.tableLoading = true;
      getRequest("/systemLog/list", this.search)
        .then(res => {
          if (res.data.code == 200) {
            this.logs = res.data.data.records;
            this.total = res.data.data.total;
            this.tableLoading = false;
          }
        })
        .catch(err => {
          this.tableLoading = false;
          console.log(err);
        });
    },

    tagType(method) {
      if (method == "GET") return "";
      else if (method == "POST") return "success";
    }
  },
  watch: {
    "search.pageNum"() {
      this.getPage();
    },
    "search.pageSize"() {
      this.search.pageNum = 1;
      this.getPage();
    }
  },

  created() {
    this.getPage();
  }
};
</script>

<style scoped>
.el-form-item {
  margin-bottom: 10px;
}
.el-pagination {
  padding: 10px 10px;
}
</style>
