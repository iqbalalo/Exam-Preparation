$(document).ready(function () {

    $('[data-toggle="popover"]').popover();
    $('.visiting-card-icon').popover({
        html: true,
        content: function () {
            return '<img>';
        }
    });
    $('.photo-icon').popover({
        html: true,
        content: function () {
            return '<img>';
        }
    });

    window.setTimeout(function () {
        $(".alert").alert('close');
    }, 30 * 1000);

    $(".datepicker").datepicker({
        dateFormat: 'yy-mm-dd',
        yearRange: 'c-70:c+5',
        changeMonth: true,
        changeYear: true,
        showButtonPanel: true
    });

    $("#selectAllCheckBox").click(function () {
        if (this.checked) { // check select status
            $('.selectRecordCheckBox').each(function () { //loop through each checkbox
                this.checked = true;  //select all checkboxes with class "checkbox1"
            });
        } else {
            $('.selectRecordCheckBox').each(function () { //loop through each checkbox
                this.checked = false; //deselect all checkboxes with class "checkbox1"
            });
        }
    });
});

