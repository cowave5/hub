<template>
  <i-frame :src="url" />
</template>
<script>
import iFrame from "@/components/iFrame/index";
export default {
  name: "smart-quartz",
  components: { iFrame },
  data() {
    return {
      url: process.env.VUE_APP_BASE_API + "/quartz/doc/index.html"
    };
  },
};
</script>
