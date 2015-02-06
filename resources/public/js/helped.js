var help_id;
var helper_id;
$(document).ready(function(){
  // パラメータ処理
  var params = getUrlVars();
  if (params['help_id'] && params['helper_id'])  {
    help_id = params['help_id'];
    helper_id = params['helper_id'];
    getHelpInfo();
  } else {
    swal("不正な画面表示です！")
  }

  $(document).on('click', '#helpbtn', helped);
});

function getUrlVars()
{
  var vars = [], hash;
  var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
  for(var i = 0; i < hashes.length; i++) {
    hash = hashes[i].split('=');
    vars.push(hash[0]);
    vars[hash[0]] = hash[1];
  }
  return vars;
}

function getHelpInfo() {
  var url = "/helpinfo?help_id=" + help_id + "&helper_id=" + helper_id;
  $.ajax({
    type: 'GET',
    url: url,
    dataType: 'json',
    success: function(json){
      $("#severity").text(json.severity);
      $("#datetime").text(json.datetime);
      $("#need_help_name").text(json.need_help_name);
      $("#need_help_tel").text(json.need_help_tel);
      $("#need_help_address").text(json.need_help_address);
      if(json.helped) {
        $('#helpbtn').hide();
        $('#status').text('ヘルパー急行済みです！');
      }

      var gurl = "http://maps.googleapis.com/maps/api/staticmap?center="
       + json.latitude + "," + json.longitude
       + "&zoom=15&format=png&sensor=false&size=480x480&maptype=roadmap&markers="
       + json.latitude + "," + json.longitude;

      var gmap = "http://maps.google.com/maps?q="
       + json.latitude + "," + json.longitude;

      $("#gsrc").attr({'src':gurl});
      $("#gmap").attr({'href': gmap});
    }
  });
}

function helped() {
  var url = "/helped?help_id=" + help_id + "&helper_id=" + helper_id;
  $.ajax({
    type: 'GET',
    url: url,
    dataType: 'json',
    success: function(json){
      if(json.success) {
        swal("登録しました！")
      } else {
        swal("他の人が登録したようです！")
      }
      $('#helpbtn').hide();
      $('#status').text('ヘルパー急行済みです！');
    }
  });
}
