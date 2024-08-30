function createGradientBg() {
  // 创建 Canvas 并绘制渐变
  const canvas = document.createElement('canvas');
  canvas.width = 1024;
  canvas.height = 1024;
  const context = canvas.getContext('2d');

  // 创建线性渐变
  const gradient = context.createLinearGradient(0, 0, 0, canvas.height);
  gradient.addColorStop(0, '#010101'); // 黑色
  gradient.addColorStop(1, '#7575e8'); // 紫色

  // 将渐变应用到 Canvas
  context.fillStyle = gradient;
  context.fillRect(0, 0, canvas.width, canvas.height);

  // 创建纹理并将其设置为场景的背景
  const texture = new THREE.CanvasTexture(canvas);

  return texture;
}

var animate = function () {
  requestAnimationFrame(animate);
  controls.update();
  renderer.render(scene, camera);
};

var loadModel = function (path, filename) {
    var name = filename;

    // Load MTL and OBJ files
    var mtlLoader = new THREE.MTLLoader();
    mtlLoader.setPath(path);
    mtlLoader.load(name + '.mtl', function (materials) {

        console.log(materials);

        materials.preload();

        var objLoader = new THREE.OBJLoader();
        objLoader.setMaterials(materials);
        objLoader.setPath(path);
        objLoader.load(name + '.obj', function (object) {

              // 遍历模型的所有子对象
              object.traverse(function (child) {
                if (child.isMesh) {
                  // 设置材质的side属性为THREE.DoubleSide
                  child.material.side = THREE.DoubleSide;
                }
              });

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

    return "true";
};



// 创建场景、摄像机和渲染器
var scene = new THREE.Scene();
//scene.background = new THREE.Color(0xFAFAFA); // Light Gray
scene.background = createGradientBg();

var camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
camera.position.z = 3;

var renderer = new THREE.WebGLRenderer();
renderer.setSize(window.innerWidth, window.innerHeight);
document.body.appendChild(renderer.domElement);

// 创建灯光
const ambientLight = new THREE.AmbientLight(0xffffff, 0.9);
scene.add(ambientLight);

const directionalLight = new THREE.DirectionalLight(0xffffff, 0.3);
directionalLight.castShadow = true;
directionalLight.position.set(10, 10, 10).normalize();
scene.add(directionalLight);

// 创建OrbitControls来控制模型旋转
const controls = new THREE.OrbitControls(camera, renderer.domElement);
// 设置缩放限制
controls.minDistance = 1; // 最小距离
controls.maxDistance = 10; // 最大距离
// 如果OrbitControls改变了相机参数，重新调用渲染器渲染三维场景
controls.addEventListener('change', function () {
    renderer.render(scene, camera); //执行渲染操作
});//监听鼠标、键盘事件


animate();

//loadModel();
