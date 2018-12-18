"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var platform_1 = require("nativescript-angular/platform");
var core_1 = require("@angular/core");
var app_module_1 = require("./app.module");
core_1.enableProdMode();
platform_1.platformNativeScriptDynamic({ createFrameOnBootstrap: true }).bootstrapModule(app_module_1.AppModule);
