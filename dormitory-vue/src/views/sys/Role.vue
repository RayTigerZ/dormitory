<template>
  <div>
    <div class="toolbar">
      <el-button
        size="medium"
        type="primary"
        icon="el-icon-plus"
        @click="handleDialog('add')"
      >
        增加角色
      </el-button>
    </div>

    <el-table
      :data="roles"
      stripe
      border
      highlight-current-row
      :header-cell-style="headerStyle"
    >
      <el-table-column type="index" width="100" align="center">
      </el-table-column>

      <el-table-column
        prop="nameZh"
        label="中文名称"
        width="180"
        align="center"
      >
      </el-table-column>
      <el-table-column prop="name" label="英文名称" width="180" align="center">
      </el-table-column>
      <el-table-column
        prop="description"
        label="描述"
        width="320"
        align="center"
      >
      </el-table-column>

      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button
            size="medium"
            type="primary"
            icon="el-icon-edit"
            @click="handleDialog('edit', scope.row)"
            title="编辑"
          >
          </el-button>
          <el-button
            size="medium"
            type="primary"
            icon="el-icon-setting"
            title="权限管理"
            @click="editPerssion(scope.row.id)"
          >
          </el-button>
          <el-button
            size="medium"
            type="danger"
            icon="el-icon-delete"
            title="删除"
            @click="handleDelete(scope.row.id)"
          ></el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      :title="roleDialog.title"
      :visible.sync="roleDialog.visible"
      width="40%"
    >
      <el-form
        ref="roleForm"
        :model="roleDialog.role"
        label-position="left"
        label-width="80px"
        :rules="roleRules"
        status-icon
      >
        <el-form-item label="中文名称" prop="nameZh">
          <el-input v-model="roleDialog.role.nameZh" autocomplete="off">
          </el-input>
        </el-form-item>
        <el-form-item label="英文名称" prop="name">
          <el-input
            v-model="roleDialog.role.name"
            autocomplete="off"
          ></el-input>
        </el-form-item>

        <el-form-item label="角色描述" prop="description">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 5 }"
            placeholder="请输入描述内容"
            v-model="roleDialog.role.description"
          >
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="roleDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="save()">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="权限管理"
      :visible.sync="perssionDialog.visible"
      width="40%"
    >
      <el-tree
        :data="perssionDialog.menu"
        node-key="id"
        :default-checked-keys="perssionDialog.ids"
        show-checkbox
        ref="menuTree"
      >
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span class="tree-node">{{ data.name }}</span>
          <span> </span>
        </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="perssionDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="savePerssion()">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getRequest, postRequest } from "@/utils/request";
import { Message } from "element-ui";

export default {
  data() {
    return {
      roles: [],
      pageNum: 1,
      pageSize: 10,
      roleDialog: {
        visible: false,
        title: "",
        role: {
          id: "",
          name: "",
          nameZh: "",
          description: ""
        }
      },
      roleRules: {
        name: [
          { required: true, message: "请输入角色英文名称", trigger: "blur" },
          { min: 1, max: 20, message: "长度在 1 到 20 个字符", trigger: "blur" }
        ],
        nameZh: [
          { required: true, message: "请输入角色中文名称", trigger: "blur" },
          { min: 1, max: 20, message: "长度在 1 到 20 个字符", trigger: "blur" }
        ],
        description: [
          { required: true, message: "请输入角色描述", trigger: "blur" },
          {
            min: 1,
            max: 200,
            message: "长度在 1 到 200 个字符",
            trigger: "blur"
          }
        ]
      },
      perssionDialog: {
        visible: false,
        menu: [],
        ids: [],
        roleId: 0
      },
      headerStyle: { "background-color": "#c6c6c6", color: "#42464e" }
    };
  },
  methods: {
    getRoles() {
      let params = {
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      getRequest("/role/list", params)
        .then(res => {
          if (res.data && res.data.code == 200 && res.data.data) {
            this.roles = res.data.data;
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    handleDialog(type, role) {
      this.roleDialog.visible = true;
      if (type == "edit") {
        this.roleDialog.title = "编辑角色";
        this.roleDialog.role = role;
      } else if (type == "add") {
        this.roleDialog.title = "增加角色";
        this.roleDialog.role = {
          id: "",
          name: "",
          nameZh: "",
          description: ""
        };
      }
    },
    save() {
      postRequest("/role/save", this.dialogRole)
        .then(res => {
          if (res.data.code == 200) {
            this.roleDialog.visible = false;
            this.getRoles();
          } else {
            Message.error(res.data.msg);
          }
        })
        .catch(err => {
          Message.error(err);
        });
    },
    handleDelete(roleId) {
      this.$confirm("此操作将永久删除该角色, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          postRequest("/role/delete", { roleId })
            .then(res => {
              if (res.data.msg) {
                Message.success(res.data.msg);
                this.getRoles();
              } else {
                Message.error(res.data.msg);
              }
            })
            .catch(err => {
              Message.error(err);
            });
        })
        .catch(() => {
          Message.info("已取消删除");
        });
    },
    editPerssion(roleId) {
      this.perssionDialog.visible = true;
      this.perssionDialog.roleId = roleId;

      getRequest("/permission/ofRole", { roleId })
        .then(res => {
          if (res.data.code == 200) {
            this.perssionDialog.menu = [res.data.data.allPermission];
            this.perssionDialog.ids = res.data.data.ids;
          }
        })
        .catch(err => {
          console.log(err);
        });
    },

    savePerssion() {
      let ids = this.$refs["menuTree"].getCheckedKeys(true);
      let roleId = this.perssionDialog.roleId;
      console.log(ids);
      postRequest("/permission/set", { roleId, ids })
        .then(res => {
          if (res.data.code == 200) {
            Message.success(res.data.msg);
            this.perssionDialog.visible = false;
          }
        })
        .catch(err => {
          console.log(err);
        });
    }
  },
  computed: {
    roleDialogVisible() {
      return this.roleDialog.visible;
    }
  },
  watch: {
    roleDialogVisible(val, oldVal) {
      if (oldVal == true) {
        this.$refs["roleForm"].clearValidate();
      }
    }
  },

  created() {
    this.getRoles();
  }
};
</script>

<style scoped>
.toolbar {
  margin-bottom: 10px;
  /* margin: 25px; */
}
</style>
