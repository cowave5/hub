<template>
  <i-frame :src="url" />
</template>
<script>
import iFrame from "@/components/iFrame/index";
export default {
  name: "monitor-application",
  components: { iFrame },
  data() {
    let url = '';
      if (process.env.NODE_ENV === 'production') {
        url = 'http://' + location.hostname +':83/health/applications';
      }else{
        url = 'http://10.64.4.74:83/health/applications';
      }
    return {
        url
    };
  },
};
</script>
