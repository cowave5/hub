<template>
  <i-frame :src="url" />
</template>
<script>
import iFrame from "@/components/iFrame/index";
export default {
  name: "monitor-nacos",
  components: { iFrame },
  data() {
    let url = '';
      if (process.env.NODE_ENV === 'production') {
        url = 'http://' + location.host + '/nacos';
      }else{
        url = 'http://10.64.4.74:8848';
      }
    return {
        url
    };
  },
};
</script>
