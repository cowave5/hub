<template>
  <i-frame :src="url" />
</template>
<script>
import iFrame from "@/components/iFrame/index";
export default {
  name: "monitor-prometheus",
  components: { iFrame },
  data() {
    let url = '';
      if (process.env.NODE_ENV === 'production') {
        url = 'http://' + location.hostname +':85';
      }else{
        url = 'http://10.64.4.74:9090';
      }
    return {
        url
    };
  },
};
</script>
