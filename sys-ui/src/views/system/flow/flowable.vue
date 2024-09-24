<template>
  <i-frame :src="url" />
</template>
<script>
import iFrame from "@/components/iFrame/index";
export default {
  name: "test",
  components: { iFrame },
  data() {
    return {
      url: 'http://' + location.host +':81/flow'
    };
  },
};
</script>
