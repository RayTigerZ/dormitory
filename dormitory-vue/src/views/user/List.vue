<template>
  <div>
    <div class="toolbar">
      <el-form :inline="true" :model="search">
        <el-form-item
          ><el-input
            v-model="search.account"
            placeholder="输入查询的帐号"
          ></el-input
        ></el-form-item>
        <el-form-item>
          <el-select v-model="search.roleId" clearable placeholder="请选择角色">
            <el-option
              v-for="item in roles"
              :key="item.id"
              :label="item.nameZh"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="getUsers()"
            >搜索</el-button
          >
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            @click="batchDialog.visible = true"
            icon="el-icon-document"
            >批量导入</el-button
          >
        </el-form-item>
      </el-form>
    </div>

    <el-table
      :data="users"
      border
      highlight-current-row
      :header-cell-style="headerStyle"
    >
      <el-table-column prop="account" label="账户" align="center" width="140">
      </el-table-column>
      <el-table-column prop="name" label="姓名" align="center" width="120">
      </el-table-column>
      <el-table-column prop="phone" label="手机号码" align="center" width="120">
      </el-table-column>
      <el-table-column prop="email" label="邮箱" align="center">
      </el-table-column>
      <el-table-column prop="sex" label="性别" align="center" width="80">
      </el-table-column>
      <el-table-column prop="college" label="学院" align="center">
      </el-table-column>
      <el-table-column prop="cla" label="班级" align="center">
      </el-table-column>

      <el-table-column label="操作" align="center" min-width="160">
        <template slot-scope="scope">
          <el-button
            size="medium"
            type="primary"
            icon="el-icon-edit"
            title="编辑"
          >
          </el-button>
          <el-button
            size="medium"
            type="danger"
            icon="el-icon-delete"
            @click="enable(scope.row.id)"
            title="删除"
          >
          </el-button>
        </template>
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

    <el-dialog title="批量导入" :visible.sync="batchDialog.visible" width="40%">
      <el-form>
        <el-form-item>
          <el-button type="primary" @click="download()" icon="el-icon-download"
            >下载批量导入模板</el-button
          >
        </el-form-item>
        <el-form-item>
          <el-upload
            ref="upload"
            action=""
            :auto-upload="false"
            :limit="1"
            :file-list="files"
            :http-request="uploadBatch"
          >
            <el-button type="primary" icon="el-icon-upload">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">
              只能上传excel文件！！
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitUpload">确认上传</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { getRequest } from "@/utils/request";
import { uploadBatch } from "@/api/user";
import { downloadBatchExcel } from "@/api/download";

export default {
  data() {
    return {
      users: [],
      search: {
        pageNum: 1,
        pageSize: 10,
        account: "",
        roleId: ""
      },
      total: 0,
      roles: [],
      files: [],
      batchDialog: {
        visible: false
      },
      headerStyle: { "background-color": "#c6c6c6", color: "#42464e" }
    };
  },
  methods: {
    getUsers() {
      getRequest("/user/list", this.search)
        .then(res => {
          if (res.data.code == 200) {
            this.users = res.data.data.records;
            this.total = res.data.data.total;
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    getRoles() {
      getRequest("/role/list", {
        pageNum: 1,
        pageSize: 100
      }).then(res => {
        if (res.data.code == 200) {
          this.roles = res.data.data;
        }
      });
    },
    download() {
      let code = "userBatch";
      downloadBatchExcel(code);
    },

    uploadBatch() {
      let data = new FormData();
      //   debugger;
      console.log(this.$refs.upload);
      data.append("file", this.$refs.upload.uploadFiles[0].raw);
      uploadBatch(data);
    },
    submitUpload() {
      this.$refs.upload.submit();
    }
  },
  created() {
    this.getUsers();
    this.getRoles();
  }
};
</script>

<style scoped>
.toolbar {
  margin-bottom: 0px;
}
.el-form-item {
  margin-bottom: 10px;
}
</style>
