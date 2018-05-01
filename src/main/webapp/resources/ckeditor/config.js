/* global CKEDITOR */

CKEDITOR.editorConfig = function (config) {
    config.extraPlugins = 'base64image';
    config.toolbar = [['Source', '-', 'Bold', 'Italic', 'Subscript', 'Superscript', 'TextColor', 'base64image']];
    config.height = '5em';
    config.enterMode = CKEDITOR.ENTER_BR;
    config.shiftEnterMode = CKEDITOR.ENTER_P;
    config.autoParagraph = false;
};
