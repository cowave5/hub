<template>
  <i-frame :src="url" />
</template>
<script>
import iFrame from "@/components/iFrame/index";
export default {
  name: "smart-code",
  components: { iFrame },
  data() {
    return {
      url: process.env.VUE_APP_BASE_API + "/code/doc/index.html"
    };
  },
};
</script>
