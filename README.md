# DecathlonDemo

<style type="text/css">
    .box:hover > .pin-text {
    display: block;
  }

  .box {
    top : 260px ;
    left : 210px;
    width:8%;
    height:8%;
    background-image:   url('http://www.clker.com/cliparts/W/0/g/a/W/E/map-pin-red.svg');
    background-position: top;
    background-repeat: no-repeat;
    background-size: contain;
    position: absolute; 
  }

  .pin-text {
    position: absolute;
    top:50%;
    transform:translateY(-50%);
    left:75%;
    white-space:nowrap;
    display: none;
  }
</style>
<img src="istockphoto-1288292255-612x612.jpeg" usemap="#floor2Map">    
        <map name="floor2Map">
            <area shape="poly" coords="210,340,216,234,312,236,323,323,317,346" matTooltip="Info about the Device" >
            <div id='pin-1' class="box" onclick="alert('1')" ></div>
        </map>
