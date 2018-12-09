<!-- <img src=screenshots/starwars_small.gif width=100% /> -->

# Multi-Label Image Annotation Using Tensorlow Object Detection API
Multi-label image annotator trained on a subset of corel-5k dataset. I manually labeled the images using [labelImg](https://github.com/tzutalin/labelImg). For test images, a text file is generated annotating each image with the most significant objects present in that image.

**Results**
<!-- ![](https://cdn-images-1.medium.com/max/1600/1*uCdxGFAuHpEwCmZ3iOIUaw.png) -->
<img src=screenshots/test.jpg />

## Folder Structure
* __Image Files:__ Both training and validation data are in `images` folder 
* __Annotation XMLs:__ Manually annotated XML files are in `annotations/xmls` folder. `annotations/train.txt` contains the training image names and `annotations/test.txt` contains the validation image names. I wrote [this](script.java) script to map each label with an integer id. This mapping is written in `annotations/label_map.pbtxt`
* __Inference:__ For testing purpose, images are kept in `test_images` folder. After the program finishes running, annotated images are generated in `output/test_images` folder. 


## Installation

First, with python and pip installed, install the scripts requirements:

```bash
pip install -r requirements.txt
```
Then you must compile the Protobuf libraries:

```bash
protoc object_detection/protos/*.proto --python_out=.
```

Add `models` and `models/slim` to your `PYTHONPATH`:

```bash
export PYTHONPATH=$PYTHONPATH:`pwd`:`pwd`/slim
```

>_**Note:** This must be ran every time you open terminal, or added to your `~/.bashrc` file._


## Usage
### 1) Create the TensorFlow Records
Run the script:

```bash
python object_detection/create_tf_record.py
```

Once the script finishes running, you will end up with a `train.record` and a `val.record` file. This is what we will use to train the model.

### 2) Download a Base Model
Training an object detector from scratch can take days, even when using multiple GPUs! In order to speed up training, we’ll take an object detector trained on a different dataset, and reuse some of it’s parameters to initialize our new model.

I used `faster_rcnn_resnet101_coco` for the demo from [model zoo](https://github.com/bourdakos1/Custom-Object-Detection/blob/master/object_detection/g3doc/detection_model_zoo.md).

Extract the files and move all the `model.ckpt` to our models home directory.



### 3) Train the Model
Run the following script to train the model:

```bash
python object_detection/train.py \
        --logtostderr \
        --train_dir=train \
        --pipeline_config_path=faster_rcnn_resnet101.config
```

### 4) Export the Inference Graph
When you model is ready depends on your training data, the more data, the more steps you’ll need.
You can test your model every ~5k steps to make sure you’re on the right path.

You can find checkpoints for your model in `train` folder.

Move the model.ckpt files with the highest number to the root of the repo:
- `model.ckpt-STEP_NUMBER.data-00000-of-00001`
- `model.ckpt-STEP_NUMBER.index`
- `model.ckpt-STEP_NUMBER.meta`

In order to use the model, you first need to convert the checkpoint files (`model.ckpt-STEP_NUMBER.*`) into a frozen inference graph by running this command:

```bash
python object_detection/export_inference_graph.py \
        --input_type image_tensor \
        --pipeline_config_path faster_rcnn_resnet101.config \
        --trained_checkpoint_prefix model.ckpt-STEP_NUMBER \
        --output_directory output_inference_graph
```

You should see a new `output_inference_graph` directory with a `frozen_inference_graph.pb` file.

### 5) Test the Model
Just run the following command:

```bash
python object_detection/object_detection_runner.py
```

It will run your object detection model found at `output_inference_graph/frozen_inference_graph.pb` on all the images in the `test_images` directory and output the results in the `output/test_images` directory.

## Useful Resources
I followed [this](https://medium.freecodecamp.org/tracking-the-millenium-falcon-with-tensorflow-c8c86419225e) excellent blog post on how to use Google Object Detection API with custom dataset.  



