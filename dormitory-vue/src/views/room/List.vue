<template>
  <div>
    <div class="buildind-div">
      <el-button
        v-for="item in buildings"
        class="building-btn"
        :key="item.id"
        :plain="item.id != search.buildingId"
        type="success"
        @click="search.buildingId = item.id"
      >
        {{ item.name }}
      </el-button>
    </div>
    <div>
      <el-button
        type="primary"
        icon="el-icon-plus"
        @click="buildingDialog.visible = true"
        >增加楼宇</el-button
      >
      <el-button
        type="primary"
        icon="el-icon-plus"
        @click="roomDialog.visible = true"
        >增加宿舍</el-button
      >
      <el-button
        type="primary"
        icon="el-icon-plus"
        @click="roomBatchDialog.visible = true"
        >批量导入宿舍</el-button
      >
    </div>

    <el-row>
      <el-col :span="4">
        <div class="title-header">楼层</div>
        <div class="floor-body">
          <template v-for="floor in floors">
            <div
              :key="floor.num"
              class="floor"
              @click="search.floor = floor.num"
              :class="{ active: floor.num == search.floor }"
            >
              <div class="floor-left">
                <span class="floor-num">{{ floor.num }}楼</span>
                <div class="floor-info">
                  <div>已入住：{{ floor.lived }}</div>
                  <div>余：{{ floor.free }}</div>
                </div>
              </div>
              <div class="floor-right">
                <span class="el-icon-arrow-right"></span>
              </div>
            </div>
          </template></div
      ></el-col>
      <el-col :span="20">
        <div class="title-header">宿舍</div>
        <div class="room-body">
          <el-collapse v-model="activeName" accordion>
            <template v-for="room in rooms">
              <el-collapse-item :key="room.number" :name="room.number">
                <template slot="title">
                  <span>{{ room.number }}宿舍</span>（{{
                    room.students.length
                  }}人入住，共有{{ room.size }}个床位）
                </template>
                <div class="beds">
                  <template v-for="index in room.size">
                    <div
                      :key="index"
                      class="bed"
                      :class="{ 'bed-lived': room.students.length > index }"
                    >
                      <svg class="icon bed-icon" aria-hidden="true">
                        <use xlink:href="#icon-danjianchuangwei"></use>
                      </svg>
                      <template v-if="room.students.length > index">
                        学生:{{ room.students[index].name }} 所属班级:{{
                          room.students[index].cla
                        }}
                      </template>
                      <template v-else>
                        <div class="nolived">
                          暂无分配
                        </div>
                      </template>
                    </div>
                  </template>
                </div>
              </el-collapse-item>
            </template>
          </el-collapse>
        </div>
      </el-col>
    </el-row>
    <!-- 弹出窗：增加楼宇 -->
    <el-dialog
      :visible.sync="buildingDialog.visible"
      width="40%"
      title="增加楼宇"
    >
      <el-form
        :model="buildingDialog.building"
        label-position="left"
        label-width="80px"
        ref="buildingForm"
        :rules="buildingRules"
      >
        <el-form-item label="楼宇名称" prop="name">
          <el-input
            type="text"
            v-model="buildingDialog.building.name"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="buildingDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="saveBuilding()">确定</el-button>
      </div>
    </el-dialog>
    <!-- 弹出窗：增加宿舍 -->
    <el-dialog :visible.sync="roomDialog.visible" width="30%" title="增加宿舍">
      <el-form
        :model="roomDialog.room"
        label-width="80px"
        label-position="left"
        :rules="roomRules"
      >
        <el-form-item label="宿舍号" prop="number">
          <el-input type="text" v-model="roomDialog.room.number"></el-input>
        </el-form-item>
        <el-form-item label="最大可住">
          <el-input type="text" v-model="roomDialog.room.size"></el-input>
        </el-form-item>
        <el-form-item label="宿舍楼">
          <el-input type="text" v-model="roomDialog.room.buildingId">
            <template slot="append"
              >栋</template
            >
          </el-input>
        </el-form-item>
      </el-form>
    </el-dialog>
    <!-- 弹出窗：批量导入宿舍 -->
    <el-dialog
      :visible.sync="roomBatchDialog.visible"
      width="40%"
      title="批量导入宿舍"
    >
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
import { getBuildings, saveBuilding, getFloors } from "@/api/building";
import { getRooms, uploadBatch } from "@/api/room";

import { downloadBatchExcel } from "@/api/download";

export default {
  data() {
    return {
      search: {
        buildingId: 0,
        floor: 0
      },
      activeName: "",
      buildings: [],
      floors: [],
      rooms: [],
      buildingDialog: {
        visible: false,
        building: {
          name: ""
        }
      },
      roomDialog: {
        visible: false,
        room: {
          number: "",
          size: "",
          buildingId: ""
        }
      },
      roomBatchDialog: {
        visible: false
      },

      buildingRules: {
        name: [{ required: true, message: "请输入楼宇名称", trigger: "blur" }]
      },
      roomRules: {
        number: [
          {
            required: true,
            len: 6,
            message: "请输入六位宿舍号",
            trigger: "blur"
          }
        ]
      }
    };
  },
  methods: {
    getBuildings() {
      getBuildings().then(res => {
        this.buildings = res.data.data;
        this.search.buildingId = this.buildings[0].id;
      });
    },
    getFloors() {
      getFloors(this.search.buildingId)
        .then(res => {
          this.floors = res.data.data;
          if (this.floors.length > 0) {
            this.search.floor = this.floors[0].num;
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    getRooms() {
      getRooms(this.search.buildingId, this.search.floor)
        .then(res => {
          if (res.data.code == 200) {
            this.rooms = res.data.data;
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    saveBuilding() {
      this.$refs["buildingForm"].validate(valid => {
        if (valid) {
          saveBuilding(this.buildingDialog.building).then(res => {
            if (res.data.code == 200) {
              this.$message.success(res.data.msg);
              this.getBuildings();
              this.buildingDialog.visible = false;
            } else {
              this.$message.error(res.data.msg);
            }
          });
        }
      });
    },
    download() {
      let code = "roomBatch";
      downloadBatchExcel(code);
    },

    uploadBatch() {
      let data = new FormData();

      data.append("file", this.$refs.upload.uploadFiles[0].raw);
      uploadBatch(data);
    },
    submitUpload() {
      this.$refs.upload.submit();
    }
  },
  watch: {
    "buildingDialog.visible"(newVal) {
      if (newVal == false) {
        this.$refs["buildingForm"].resetFields();
      }
    },
    "search.buildingId"() {
      this.getFloors();
      this.getRooms();
    },
    "search.floor"() {
      this.getRooms();
    }
  },

  created() {
    this.getBuildings();
  }
};
</script>

<style scoped>
.buildings-div {
  display: flex;
  flex-wrap: wrap;
}
.building-btn {
  height: 40px;
  width: 90px;
  margin: 8px 20px 8px 0px;
}
.building-btn span {
  height: 100%;
  line-height: 2.5;
}

.el-row {
  margin-top: 20px;
  background-color: #fff;
}

.title-header {
  border-left: 3px #67c23a solid;
  padding-left: 10px;
  height: 45px;
  font-size: 17px;
  line-height: 45px;
  margin-bottom: 10px;
}
.floor {
  padding: 8px 10px;
  background-color: #fff;
  display: flex;
  justify-content: space-between;
}
.floor-left {
  width: 80%;
}
.floor-right {
  line-height: 50.8px;
}
.el-icon-arrow-right {
  font-size: 20px;
}

.active {
  border-left: 3px #67c23a solid;
  background-color: #fafafa;
}
.floor-num {
  font-size: 18px;
}
.floor-info {
  padding-top: 10px;
  font-size: 13px;
  font-weight: 400;
  color: #c0c4cc;
  display: flex;
  justify-content: space-between;
}
.room-body {
  padding: 0px 15px;
}
.beds {
  display: flex;
  flex-wrap: wrap;
}
.bed {
  padding: 0px 20px;
  margin: 10px 70px;
  background-color: #ebeef5;
}
.bed-lived {
  background-color: #67c23a;
}
.bed-icon {
  width: 8em;
  height: 8em;
  color: #c0c4cc;
}
.nolived {
  margin: 8px;
  height: 20px;
  line-height: 20px;
  text-align: center;
}
</style>
