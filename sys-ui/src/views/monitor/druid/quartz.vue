<template>
  <i-frame :src="url" />
</template>
<script>
import iFrame from "@/components/iFrame/index";
export default {
  name: "Druid-quartz",
  components: { iFrame },
  data() {
    return {
      url: process.env.VUE_APP_BASE_API + "/quartz/druid"
    };
  },
};
</script>
