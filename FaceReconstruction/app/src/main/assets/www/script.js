

// 创建场景、摄像机和渲染器
var scene = new THREE.Scene();
var camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
var renderer = new THREE.WebGLRenderer();
renderer.setSize(window.innerWidth, window.innerHeight);
document.body.appendChild(renderer.domElement);
 
// 创建灯光
const ambientLight = new THREE.AmbientLight(0x404040);
scene.add(ambientLight);
 
const directionalLight = new THREE.DirectionalLight(0xffffff);
directionalLight.position.set(1, 1, 1).normalize();
scene.add(directionalLight);

var path = "file:///android_asset/www/models/"
// Load MTL and OBJ files
var mtlLoader = new THREE.MTLLoader();
mtlLoader.setPath(path);
mtlLoader.load('face.obj.mtl', function (materials) {

  console.log(materials);

  materials.preload();

  var objLoader = new THREE.OBJLoader();
  objLoader.setMaterials(materials);
  objLoader.setPath(path);
  objLoader.load('face.obj', function (object) {
    scene.add(object);

    animate();
  },
    function (xhr) {
      console.log((xhr.loaded / xhr.total * 100) + '%  obj loaded');
    },
    function (error) {
      console.error('An error occurred while loading the OBJ file', error);
    });
});

camera.position.z = 3;

// 创建OrbitControls来控制模型旋转
const controls = new THREE.OrbitControls(camera, renderer.domElement);
// 如果OrbitControls改变了相机参数，重新调用渲染器渲染三维场景
controls.addEventListener('change', function () {
  renderer.render(scene, camera); //执行渲染操作
});//监听鼠标、键盘事件


var animate = function () {
  requestAnimationFrame(animate);
  controls.update();
  renderer.render(scene, camera);
};

animate();
