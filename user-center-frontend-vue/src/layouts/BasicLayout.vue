<template>
  <div id="basicLayout">
    <a-layout>
      <a-layout-header class="header">
        <GlobalHeader/>
      </a-layout-header>
      <a-layout-content class="content">
        <router-view/>
      </a-layout-content>
      <a-layout-footer class="footer">
        <span>用户中心 · Spring Boot / Vue</span>
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script setup lang="ts">
import GlobalHeader from "@/components/GlobalHeader.vue";

</script>

<style scoped>
#basicLayout .footer{
  background: #efefef;
  text-align: center;
  padding: 16px;
}
#basicLayout .content{
  padding: 20px;
  min-height: calc(100vh - 128px);
  background: linear-gradient(to right, #fefefe, #fff);
}
#basicLayout .header{
  background: white;
  margin-bottom: 16px;
  color: unset;
  padding-inline: 20px;
}
</style>
