<template>
  <div>
    <a-input-search
      style="max-width: 320px"
      v-model:value="searchValue"
      placeholder="输入用户名搜索"
      enter-button="搜索"
      size="large"
      @search="onSearch"
    />
    <a-table :columns="columns" :data-source="data" :loading="loading" row-key="id">
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'avatarUrl'">
          <a-image :src="record.avatarUrl" :width="120" />
        </template>
        <template v-else-if="column.dataIndex === 'userRole'">
          <div v-if="record.userRole === 1">
            <a-tag color="green">管理员</a-tag>
          </div>
          <div v-else>
            <a-tag color="blue">普通用户</a-tag>
          </div>
        </template>
        <template v-if="column.dataIndex === 'createTime'">
          {{ dayjs(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
        </template>
        <template v-else-if="column.key === 'action'">
          <a-button danger @click="doDelete(record.id)">
            删除
          </a-button>
        </template>
      </template>
    </a-table>
  </div>
</template>
<script lang="ts" setup>
import {ref} from "vue";
import {deleteUser, searchUsers} from "@/api/user.ts";
import {message} from "ant-design-vue";
import dayjs from "dayjs";
import type { User } from "@/types/user";

const columns = [
  {
    title: "id",
    dataIndex: "id",
  },
  {
    title: "用户名",
    dataIndex: "username",
  },
  {
    title: "账号",
    dataIndex: "userAccount",
  },
  {
    title: "头像",
    dataIndex: "avatarUrl",
  },
  {
    title: "性别",
    dataIndex: "gender",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
  },
  {
    title: "用户角色",
    dataIndex: "userRole",
  },
  {
    title: "操作",
    key: "action",
  },
];

// 数据
const data = ref<User[]>([]);
const loading = ref(false);

//定义搜索
const searchValue = ref("");
const onSearch = () => {
  fetchData(searchValue.value);
};


// 获取数据
const fetchData = async (username="") => {
  try {
    loading.value = true;
    const res = await searchUsers(username);
    if (res.data.code === "200" && res.data.data) {
      data.value = res.data.data;
      return;
    }
    message.error(res.data.message || "获取数据失败");
  } finally {
    loading.value = false;
  }
};
// 删除数据
const doDelete = async (id?: number) => {
  if (!id) {
    return;
  }
  const res = await deleteUser({ id });
  if (res.data.code === "200") {
    message.success("删除成功");
    await fetchData(searchValue.value);
  } else {
    message.error(res.data.message || "删除失败");
  }
};


fetchData();

</script>
