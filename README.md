# McuCommunicator API

This is the centralized repository for MCU communication on KSW devices. 
There are multiple implementations regarding what to use and when to use it, to ask our developers, please join our [Discord channel](https://discord.gg/RdHUuT8m).


# Getting started

Add this in your root `build.gradle` file:
```css
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

You can now import this library API into your project directly and interact with the classes inside. (Replace "current-release" with the proper release tag on GitHub: current=1.0.1)

```css
	dependencies {
	        implementation 'com.github.KswCarProject:McuCommunicator:current-release'
	}
```

## Current Status

**Supported MCUs:** Ksw (Serial, Reflection)



## About this library API

**Library maintainer:** [@nicholaschum](https://github.com/nicholaschum) (nicholaschum#2331)

**Current MCU maintainer and researcher:** [@Snaggly](https://github.com/Snaggly) (Snaggle#9418)

**Core Ksw investigation and research:** @VincentZ4
