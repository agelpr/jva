var show_pass     = '';
var MASTER_helper = [];
var DateToday     = new Date();
var day           = DateToday.getDate().toString();
var month         = DateToday.getMonth() + 1;
var year          = DateToday.getFullYear();
var id_licence    = null;
var flag          = null;
var FullDate      = month.toString().length == 1 ? year.toString() + '0' + month.toString() + day.toString() : year.toString() + month.toString() + day.toString();
var swalWithBootstrapButtons = Swal.mixin({
    customClass: {
      confirmButton: 'btn btn-success',
      cancelButton: 'btn btn-danger'
    },
    buttonsStyling: false
});
$(function(){

    $('.page-body, .main-nav, .toggle-sidebar').on('click', function () 
    {
        $('.nav-menus').removeClass('open');
    });
    $('.toggle-sidebar').on('click', function () 
    {
        $('.main-nav').toggleClass('hide');
        $('.main-header-left').toggleClass('hide');
        $('.page-body').toggleClass('large');
        $('.footer').toggleClass('large');
    });
    $('.menu li:has(ul)').click(function(e)
    {
        e.preventDefault();
        if ($(this).hasClass('activado')) {
            $(this).removeClass('activado');
            $(this).children('ul').slideUp();
        } else {
            $('.menu li ul').slideUp();
            $('.menu li').removeClass('activado');
            $(this).addClass('activado');
            $(this).children('ul').slideDown();
        }
    });
    $('.page-body, .home').on('click', function ()
    {
       if ($('.menu li:has(ul)').hasClass('activado')) {
            $('.menu li').removeClass('activado');
            $('.menu ul').slideUp();
        }
    });
    $(".main-nav").hover(function() {
        if ($('.main-nav').hasClass('hide')) {
            $('.main-nav').addClass('show');
        }
    }, function() {
        if ($('.main-nav').hasClass('hide')) {
            $('.main-nav').removeClass('show');
        }
    });
    $(document).ajaxStart(function () {
        LoadingWait();
    });
    $(document).ajaxStop(function () {
        $('#LoadingContent').waitMe('hide');
    });
});
(function($) {
    "use strict";
    $(".mobile-toggle").click(function()
    {
        $(".nav-menus").toggleClass("open");
    });

})(jQuery);
/*--------------------FUNCIONALIDAD GENERAL--------------------*/
function FullScreen ()
{
    if(document.fullscreen == false){
        var elem = document.documentElement;
        elem.requestFullscreen();
    }else{
        document.exitFullscreen();
    }
}
function DarkMode ()
{
    $.ajax({
        url  : '/DarkMode',
        type : 'POST',
        data : {DarkMode:$('.mode i').hasClass('fa-moon')?'1':'0'}
    })
    .done(function(response){
        $('.mode i').toggleClass("fa-moon").toggleClass("fa-lightbulb");
        $('body').toggleClass("dark-only");
    })
    .fail(function(jqXHR, textStatus, errorThrown) {
        Swal.fire(jqXHR.status.toString(), jqXHR.responseJSON.error, 'error');
    });
}
function LoadingWait ()
{
    $('#LoadingContent').waitMe({
        effect: 'img',
        text  : '',
        color : ['#000000', '#005aff', '#002c7c'],
        sizeW : '',
        sizeH : '',
        source: 'img/fs_loading.gif'
    });
}
function BtnRedirect (page)
{
    current_page = page;
    if(page == 'home'){
        $('#homepage').trigger('click');
        $('#homepage').addClass('active');
    }else{
        $('.nav-move').removeClass('active');
        $('#'+page).addClass('active');
        $('#'+page).parent().parent().parent().addClass('active-accordion');
        $.ajax({
            url     : '/'+page+"?lang="+translation,
            dataType: "html"
        })
        .done(function(html){
            if(html.search(/login-box/) < 0){
                $("#content").empty().append(html);
                ApplyFormats();
            }else{
                LogOut();
            }
        })
        .fail(function(jqXHR, textStatus, errorThrown) {
            Swal.fire(jqXHR.status.toString(), jqXHR.responseJSON.error, 'error');
        });
    }
    return false;
}
function IdleLogout () 
{
    var time = 600;// Time to session 10 minutes
    // var time = 180;// Time to session 3  minutes
    var t;
    var time_remaing    = 0;
    var time_out        = time; // value in seconds
    window.onload       = ResetTimer;
    window.onmousemove  = ResetTimer;
    window.onmousedown  = ResetTimer; // catches touchscreen presses as well      
    window.ontouchstart = ResetTimer; // catches touchscreen swipes as well 
    window.onclick      = ResetTimer; // catches touchpad clicks as well
    window.onkeypress   = ResetTimer;
    window.addEventListener('scroll', ResetTimer, true); // improved; see comments
    var myVar = setInterval(MyTimer , 1000);

    function MyTimer() 
    {
        time_remaing++;
        if (time_remaing == time_out) {
            Swal.fire({
                title: "¿Desea extender su sesión?",
                icon: 'question',
                showDenyButton: true,
                allowOutsideClick: false,
                allowEscapeKey: false,
                confirmButtonText: "Si",
                confirmButtonColor: "var(--first-color)",
                denyButtonText: "No",
                denyButtonColor: "var(--color-orange)"
            }).then((result) => {
                if (result.isConfirmed) {
                    ResetTimer();
                } else if (result.isDenied) {
                    clearInterval(myVar);
                    window.location.replace("/logout");
                }
            });
        }
        if (time_remaing == (time_out + 10)) {
            clearInterval(myVar);
            window.location.replace("/logout");
        }
    }
    function ResetTimer() 
    {
        time_remaing = 0;
    }
}
function TextAreaCollapse (input)
{
    input.style.height = Math.min(input.scrollHeight, 300) + "px";
}
function ApplyFormats (container) 
{
    if($('input[required]').prev('label').children().hasClass('required-label') == false){
        $('input[required]').prev('label').append('<span class="required-label">*</span>');
    }
    if($('select[required]').prev('label').children().hasClass('required-label') == false){
        $('select[required]').prev('label').append('<span class="required-label">*</span>');
    }    
    $('.alphanum').on('keyup', function () {
        return $(this).val($(this).val().toUpperCase());
    });
    $('.alpha').on('keyup', function () {
        return $(this).val($(this).val().toUpperCase());
    });
    $('.alphaSpecial').on('keyup', function () {
        return $(this).val($(this).val().toUpperCase());
    });
    $('.iconPicker').iconpicker({
        hideOnSelect: true
    });
    container = container || 'content';
    $('#' + container).find('.alphanum').alphanum();
    $('#' + container).find('.alpha').alpha();
    $('#' + container).find('.numeric').numeric();
    $('#' + container).find('.money').maskMoney({thousands: '.', decimal: ','});
    $('#' + container).find('.email').inputmask("email");
    $('#' + container).find('.phone').usPhoneFormat({
         format: '(xxx) xxx-xxxx'
     });
    $('#' + container).find('.ipaddress').inputmask({
        alias: "ip",
        greedy: false 
    });
    $('#' + container).find('.dateDay').datepicker({
        format: 'dd/mm/yyyy',
        autoclose: true,
        closeOnDateSelect: true,
        language: translation.replace(/['"]+/g, ''),
        endDate: new Date()
    });
    $('#' + container).find('.dateExp').datepicker({
        format: 'dd/mm/yyyy',
        autoclose: true,
        closeOnDateSelect: true,
        language: translation.replace(/['"]+/g, ''),
    });
    $('.date_range').daterangepicker({
        maxDate: new Date(),
        locale: {
            format: 'DD/MM/YYYY',
            "applyLabel": "Aceptar",
            "cancelLabel": "Cancelar",
            "daysOfWeek": [
                "Do",
                "Lu",
                "Ma",
                "Mi",
                "Ju",
                "Vi",
                "Sa"
            ],
            "monthNames": [
                "Enero",
                "Febrero",
                "Marzo",
                "Abril",
                "Mayo",
                "Junio",
                "Julio",
                "Agosto",
                "Setiembre",
                "Octubre",
                "Noviembre",
                "Diciembre"
            ]
        }
    });
    $('.select2').select2();
    $('.modal').on('shown.bs.modal', function (e) {
        $(this).find('.select2').select2({
            dropdownParent: $(this).find('.modal-content')
        });
    });
    $('.multiSelect2').select2({
        placeholder: "Seleccione..."
    });
    $('.select2, .select2M, .dateDay, .dateExp').on('change', function() {
        if($(this).val() != '') {
            $(this).valid();
        }
    });
}
function ValidatePhoneFormat (phone, target) {
    var regex = "^((\\(\\d{3}\\))|\\d{3})[- ]?\\d{3}[- ]?\\d{4}$";
    if(phone.match(regex) == null){
        toastr.error('Formato de teléfono invalido');
        $('#'+target).val('');
        return false;
    }
    return true;
}
function BeforeLogOut () 
{
    Swal.fire({
        title:'¿Desea salir del sistema y cerrar la sesión?',
        icon: 'question',
        showDenyButton: true,
        allowOutsideClick: false,
        allowEscapeKey: false,
        confirmButtonText: "Si",
        confirmButtonColor: "var(--first-color)",
        denyButtonText: "No",
        denyButtonColor: "var(--color-orange)"
    }).then((result) => {
        if (result.isConfirmed) 
        {
            LogOut();
        }
    });
}
function LogOut () 
{
    $.ajax({
        url  : '/logout',
        data : {action:'Logout'}
    })
    .done(function(response){
        location.replace("/logout");
    })
    .fail(function(jqXHR, textStatus, errorThrown) {
        Swal.fire(jqXHR.status.toString(), jqXHR.responseJSON.error, 'error');
    });
}
/*--------------------VALIDACIONES GENERALES--------------------*/
function ValidateDate (input)
{
    var DateExp = $('#'+input.id).val().split("/");
    DateExp     = new Date(DateExp[2],DateExp[1] - 1 ,DateExp[0]);
    if(DateExp < DateToday){
        toastr.error($('label[for="' + $('#'+input.id).attr('id') + '"]').attr('data-msg'), 'Documento Vencido: ');
        $('#'+input.id).val('');
    }
}
function SearchPostalCode (input, code, form)
{
    if(code != ''){
        $( "#"+input ).autocomplete({
            appendTo : form == undefined ? "#frmSteps" : '#'+form,
            minLength: 2,
            source: function(request, response) 
            {
                $.ajax({
                    url  : "/SearchPostalCode",
                    type : 'POST',
                    data : {postal_code: code},
                    success: function (data) {
                        if(data != ''){
                            response($.map(data, function (item) {
                                return {
                                    label : item.descripcion,
                                    value : item.description
                                };
                            }));
                        }else{
                            toastr.error('Código postal no existe');
                            $('#'+input).val('');
                        }
                    }
                });
            },
            select: function( event, ui ) {
              $( "#"+input ).val(ui.item.value);
            }
        });
    }
}
/*--------------------CONTRASEÑAS--------------------*/
function checkPasswordStrength (password)
{
    var number     = /([0-9])/;
    var upperCase  = /([A-Z])/;
    var lowerCase  = /([a-z])/;
    var specialCharacters = /([-,~,!,@,#,$,%,^,&,*,_,+,=,?,>,<,.])/;

    var characters     = (password.length >= 8 && password.length <= 10 );
    var capitalletters = password.match(upperCase) ? 1 : 0;
    var loweletters    = password.match(lowerCase) ? 1 : 0;
    var numbers        = password.match(number) ? 1 : 0;
    var special        = password.match(specialCharacters) ? 1 : 0;

    this.update_info('length', password.length >= 8 && password.length <= 15);
    this.update_info('capital', capitalletters);
    this.update_info('small', loweletters);
    this.update_info('number', numbers);
    this.update_info('special', special);

    var total = characters + capitalletters + loweletters + numbers + special;
    this.password_meter(total);
}
function update_info (criterion, isValid) 
{
    var $passwordCriteria = $('#passwordCriterion').find('li[data-criterion="' + criterion + '"]');
    if (isValid) {
        $passwordCriteria.removeClass('invalid').addClass('valid');
    } else {
        $passwordCriteria.removeClass('valid').addClass('invalid');
    }
}
function password_meter (total) 
{
    var meter = $('#password-strength-status');
    meter.removeClass();
    if (total === 0) {
        meter.html('');
    } else if (total === 1) {
        meter.addClass('veryweak-password').html("Muy Débil");
        $("#save_password").prop("disabled", true);
    } else if (total === 2) {
        meter.addClass('weak-password').html("Débil");
        $("#save_password").prop("disabled", true);
    } else if (total === 3) {
        meter.addClass('medium-password').html("Medio");
        $("#save_password").prop("disabled", true);
    } else if (total === 4) {
        meter.addClass('average-password').html("Promedio");
        $("#save_password").prop("disabled", true);
    } else {
        meter.addClass('strong-password').html("Fuerte");
        $("#save_password").prop("disabled", true);
    }
}   
function passwordConfirmed (password, password2) 
{
    var confirmed = password == password2 ? 1 : 0;
    resp = this.update_info2('confirmed', confirmed);

    var total = confirmed;
    this.password_meter2(total);
    return resp;
}
function update_info2 (criterion, isValid) 
{
    var $passwordCriteria = $('#passwordCriterion2').find('li[data-criterion="' + criterion + '"]');
    if (isValid) {
        $passwordCriteria.removeClass('invalid').addClass('valid');
    } else {
        $passwordCriteria.removeClass('valid').addClass('invalid');
    }
    if ($("li.valid").length==5){
        return true;
    }else{
        return false;
    }    
}
function password_meter2 (total) 
{
    var meter = $('#password-strength-status2');
    meter.removeClass();
    if (total === 0) {
        meter.addClass('veryweak-password').html("La contraseña no coinciden");
        $("#save_password").prop("disabled", true);
    }else {
        meter.addClass('strong-password').html("Confirmada");
        $("#save_password").prop("disabled", false);
    }
}
function validate_pass ()
{
    var password = $('#PASS_NEW').val();
    if(password == ''){
      $('#CONFIRM_PASSWORD').val('');
      $('.hidden_password').each(function(){
          $(this).hide();
      });
    }
    checkPasswordStrength(password);
}
function validate_pass2 (input)
{
    var password  = $('#PASS_NEW').val();
    if(password == ''){
      $('.hidden_password').each(function(){
          $(this).hide();
      });
    }else{
      $('.hidden_password').each(function(){
          $(this).show();
      });
      passwordConfirmed(password, $('#'+input.id).val());
    }
}
function showPass (valor) 
{
    if (valor == 'password') {
        if (show_pass == '') {
            $('#PASS_NEW').attr('type', 'text');
            $('#icon_password').removeClass('fa-eye-slash');
            $('#icon_password').addClass('fa-eye');
            show_pass = 'x';
        } else {
            $('#PASS_NEW').attr('type', 'password');
            $('#icon_password').removeClass('fa-eye');
            $('#icon_password').addClass('fa-eye-slash');
            show_pass = '';
        }
    }
}    
function show_confirm_password (valor)
{
    if(valor == 'password_confirmation'){
        if(show_pass == ''){
            $('#CONFIRM_PASSWORD').attr('type','text');
            $('#icon_password2').removeClass('fa-eye-slash');
            $('#icon_password2').addClass('fa-eye');
            show_pass = 'x';
        }else{
            $('#CONFIRM_PASSWORD').attr('type','password');
            $('#icon_password2').removeClass('fa-eye');
            $('#icon_password2').addClass('fa-eye-slash');
            show_pass = '';
        }
    }
}
function SavePassword () 
{
    if($('#frmChangePass').valid() == true){
        $.ajax({
            url: "/USERS/StorePassword",
            type: 'POST',
            data: $('#frmChangePass').serialize(),
        })
        .done(function (response) {
            if (response.code == 200) {
            Swal.fire('Registro exitoso','','success')
                .then(() => {
                    location.reload();
                });
            }
        })
        .fail(function(jqXHR, textStatus, errorThrown) {
            Swal.fire(jqXHR.status.toString(), jqXHR.responseJSON.error, 'error');
        });
    }
}
/*--------------------CUENTAS BANCARIAS--------------------*/
function ValidateAccNumber (input)
{
    if(CheckAcc(input.value) == false){
        toastr.error("Cuenta inválida", 'Error: ');
        $('#'+input.id).val('');
    }else{
        $.ajax({
            url  : "/ValidateAccountNumber",
            type : 'POST',
            data : {account_number:input.value}
        })
        .done(function(response){
            if(response.code == 200){
                toastr.error('Esta cuenta ya existe', 'Error: ');
                $('#'+input.id).val('');
            }
        })
        .fail(function(jqXHR, textStatus, errorThrown) {
            Swal.fire(jqXHR.status.toString(), jqXHR.responseJSON.error, 'error');
        });
    }
}
function ValidateAccCode () 
{
    if($('#account_number').val().substring(0,4) != '0172'){
        toastr.error('Número de cuenta no pertenece a Bancamiga', 'Error: ');
        return false;
    }
    return true;
}
function CheckAcc (num) 
{
    bank    = num.substr(0, 4);
    office  = num.substr(4, 4);
    digit   = num.substr(8, 2);
    acc_num = num.substr(10, 10);

    return AccountVerify(bank, office, digit, acc_num);
}
function AccountVerify (bank, office, digit, acc_num) 
{
    var pesos1 = new Array(3, 2, 7, 6, 5, 4, 3, 2);
    var pesos2 = new Array(3, 2, 7, 6, 5, 4, 3, 2, 7, 6, 5, 4, 3, 2);
    var Account = bank + office + digit + acc_num;
    if (Account.length != 20) {
        return false;
    }
    var fields1 = bank + office;
    var fields2 = office + acc_num;

    var digit1 = parseInt(fields1, 10);
    var digit2 = parseInt(fields2, 10);
    var sum1 = 0;
    var sum2 = 0;

    for (var i = 0; i < 8; i++) {

        var digit = Math.floor(((digit1 / Math.pow(10.0, (7 - i) * 1.0)))) % 10;

        sum1 += pesos1[i] * digit;

    }
    for (var i = 0; i < 14; i++) {
        var digit = Math.floor(((digit2 / Math.pow(10.0, (13 - i) * 1.0)))) % 10;
        sum2 += pesos2[i] * digit;
        var a = Math.floor(((digit2 / Math.pow(10.0, (13 - i) * 1.0))));
        var b = Math.floor(((digit2 / Math.pow(10.0, (13 - i) * 1.0)))) % 10;
    }
    var digit1 = (11 - (sum1 % 11));
    var digit2 = (11 - (sum2 % 11));
    if (digit1 >= 10 || digit1 < 1)
        digit1 = digit1 % 10;
    if (digit2 >= 10 || digit2 < 1) {
        digit2 = digit2 % 10;
    }
    var AccountValidated = bank + office + digit1 + digit2 + acc_num;

    return Account == AccountValidated;
}
/*--------------------MODALES--------------------*/
function clean_modals (value, type) 
{
    $('#id').remove();
    $('.select2').val('').trigger('change');
    $('#' + value).trigger('reset');
    $('#' + value).validate().resetForm();
    if(type == 'view'){
    $('.select2').val('').trigger('change');
        $('.frmMember').prop('disabled', false);
        $('.frmAgency').prop('disabled', false);
        $('.frmAccount').prop('disabled', false);
        $('.FrmUsers').prop('disabled', false);
        $('.frmGroups').prop('disabled', false);
        $('.frmCompany').prop('disabled', false);
        $('.frmModule').prop('disabled', false);
        $('#btn-content').prop('hidden', false);
    }
    id_company = null;
    id_country = null;
}
/*--------------------ARCHIVOS--------------------*/
function GetFile (input)
{
    var file = document.querySelector('#'+input).files[0];
    if(Validate_Base64(file) == true){
        GetBase64(file);
    }else{
        $('#'+input).val('');
    }
}
function GetBase64 (file) 
{
    var reader     = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function () {
        Base64File = reader.result;
   };
    reader.onerror = function (error) {
        console.log('Error: ', error);
   };
}
function Validate_Base64 (value)
{
    if(value == undefined){
        toastr.error('Debe seleccionar un archivo valido', 'Error: ');
        return false;
    }
    if(value.type != 'application/pdf'){
        toastr.error('El formato de los archivos debe ser en formato PDF', 'Error: ');
        return false;
    }
    if(value.size > 3000000){
        toastr.error('El tamaño máximo del archivo deber ser 3 MB', 'Error: ');
        return false;
    }
    return true;
}
function UploadFile (id_name,program)
{
    if(Validate_Collections() != false){
        var obj_collection = {
            "id_collections" : $('#collection option:selected').val(),
            "collection_dsc" : $('#collection option:selected').text(),
            "file"           : Base64File,
        }
        Array_collections.push(obj_collection);
        $.ajax({
            url  : "/UPDATES/FileUpload",
            type : 'POST',
            data : {collections:JSON.stringify(Array_collections),id_name:id_name,id:id_temp,program:program}
        })
        .done(function(response){
            if(response.code == 200){
                Array_display.push(obj_collection);
                Load_Table_Collections(Array_display);
                DisableCollections(null, $('#collection option:selected').val(), true);
                Clean_CollectionsFields();
                toastr.success('Carga Exitosa');
            }else{
                toastr.error('Carga Fallida');
                Clean_CollectionsFields();
            }
        })
        .fail(function(jqXHR, textStatus, errorThrown) {
            Swal.fire(jqXHR.status.toString(), jqXHR.responseJSON.error, 'error');
        });
    }
}
function SearchFile (id, module)
{
    $.ajax({
        url  : "/SearchFile",
        type : 'POST',
        data : {id_collections:id, id_register:Data.id, id_module:module}
    })
    .done(function(SearchResult) {
        var response = SearchResult.response;
        var file     = SearchResult.data.file;
        if (response.code == 200) {
            $('#ch'+id).removeClass('fas fa-times').addClass('fas fa-check');
            $('#Srch'+id).prop('hidden', true);
            $('#Dwn'+id).prop('hidden', false).attr('href', file);
            $('#Row'+id).removeClass('RedClass').addClass('GreenClass');
        }
    })
    .fail(function(jqXHR, textStatus, errorThrown) {
        Swal.fire(jqXHR.status.toString(), jqXHR.responseJSON.error, 'error');
    });
}
/*--------------------ACCIONES GENERALES--------------------*/
function BtnUpdate (id, url, type)
{
    $.ajax({
        url     : url,
        dataType: "html",
        data: {id: id, type:type}
    })
    .done(function(html){
        $("#content").empty().append(html);
        Load_Steps();
        ApplyFormats();
    })
    .fail(function(jqXHR, textStatus, errorThrown) {
        Swal.fire(jqXHR.status.toString(), jqXHR.responseJSON.error, 'error');
    });
}
function GetActions (type) 
{
    if(Actions != null){
        return Actions.search(type) < 0 ? "hidden" : "";
    }else{
        return "hidden";
    }
}
function CleanDrag ()
{
    $('#id').remove();
    $('#SearchPool').val('');
    $('#SearchDrop').val('');
    $('#CheckAll').prop('checked', false);
    $('#pool').empty();
    SearchDnD('SearchPool', 'pool');
    $('#drop').empty();
    SearchDnD('SearchDrop', 'drop');
}
function SearchDnD (input, children) {
    var input, filter, ul, li, a, i, txtValue;
    input = document.getElementById(input);
    filter = input.value.toUpperCase();
    ul = document.getElementById(children);
    li = ul.getElementsByTagName("div");
    for (i = 0; i < li.length; i++) {
        txtValue = li[i].textContent || li[i].innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            li[i].style.display = "";
        } else {
            li[i].style.display = "none";
        }
    }
}
/*--------------------DIRECCIONES--------------------*/
function GetState (input, target, STA) 
{
    if(input != ''){
        $.ajax({
            url  : '/GetChildrens',
            type : 'POST',
            data : {id_country:input, table:'country'}
        })
        .done(function(response){
            var state = response.state;
            for (var i = 0; i < state.length; i++) {
                $('#'+target).append('<option value="'+state[i].id+'" '+(STA == state[i].id ? 'selected': '')+'>'+state[i].description+'</option>');
            }
        })
        .fail(function(jqXHR, textStatus, errorThrown) {
            Swal.fire(jqXHR.status.toString(), jqXHR.responseJSON.error, 'error');
        });
    }else{
        $('#'+target).empty().append('<option value="" '+(STA == '' ? 'selected' : '')+'>Seleccione...</option>');
    }
}
function GetCity (input, target, CTY, MNC)
{ 
    id_country = null;
    target = target.split(",");
    if(input != ''){
        $.ajax({
            url  : '/GetChildrens',
            type : 'POST',
            data : {id_state:input, table:'city'}
        })
        .done(function(response){
            if(target[0] != 'null'){
                var city         = response.city;
                $('#'+target[0]).empty().append('<option value="" '+(CTY == '' ? 'selected' : '')+'>Seleccione...</option>');
                for (var i = 0; i < city.length; i++) {
                    $('#'+target[0]).append('<option value="'+city[i].id+'" '+(CTY == city[i].id ? 'selected': '')+'>'+city[i].description+'</option>');
                }
            }
            var municipality = response.municipality;
            $('#'+target[1]).empty().append('<option value="" '+(MNC == '' ? 'selected' : '')+'>Seleccione...</option>');
            for (var i = 0; i < municipality.length; i++) {
                $('#'+target[1]).append('<option value="'+municipality[i].id+'" '+(MNC == municipality[i].id ? 'selected': '')+'>'+municipality[i].description+'</option>');
            }
        })
        .fail(function(jqXHR, textStatus, errorThrown) {
            Swal.fire(jqXHR.status.toString(), jqXHR.responseJSON.error, 'error');
        });
    }else{
        $('#'+target[0]).empty().append('<option value="" '+(CTY == '' ? 'selected' : '')+'>Seleccione...</option>');
        $('#'+target[1]).empty().append('<option value="" '+(MNC == '' ? 'selected' : '')+'>Seleccione...</option>');
    }
}
function GetParish (input, target, PRSH) 
{
    if(input != ''){
        $.ajax({
            url  : '/GetChildrens',
            type : 'POST',
            data : {id_municipality:input, table:'parish'}
        })
        .done(function(response){
            var parish = response.parish;
            // $('#'+target).empty().append('<option value="" '+(PRSH == '' ? 'selected' : '')+'>Seleccione...</option>');
            for (var i = 0; i < parish.length; i++) {
                $('#'+target).append('<option value="'+parish[i].id+'" '+(PRSH == parish[i].id ? 'selected': '')+'>'+parish[i].description+'</option>');
            }
        })
        .fail(function(jqXHR, textStatus, errorThrown) {
            Swal.fire(jqXHR.status.toString(), jqXHR.responseJSON.error, 'error');
        });
    }else{
        $('#'+target).empty().append('<option value="" '+(PRSH == '' ? 'selected' : '')+'>Seleccione...</option>');
    }
}
function FillSelect (id, target, table, SELECTED) 
{
    if(id != ''){
        $.ajax({
            url  : '/FillSelect',
            type : 'POST',
            data : {id:id, table:table}
        })
        .done(function(response){
            $('#'+target).empty().append('<option value="" '+(SELECTED == '' ? 'selected' : '')+'>Seleccione...</option>');
            for (var i = 0; i < response.length; i++) {
                $('#'+target).append('<option value="' + response[i].id+'" ' + (SELECTED == response[i].id ? 'selected': '') + '>' + response[i].description + '</option>');
            }
        })
        .fail(function(jqXHR, textStatus, errorThrown) {
            Swal.fire(jqXHR.status.toString(), jqXHR.responseJSON.error, 'error');
        });
    }else{
        $('#'+target).empty().append('<option value="" ' + (SELECTED == '' ? 'selected' : '') + '>Seleccione...</option>');
    }
}
function ChangeDirection (value)
{
    CleanDirectionsFields();
    if(value == "85"){
        $('#national-container').prop('hidden', false);
        $('.NDir').prop('disabled', false);
        $('#international-container').prop('hidden', true);
        $('.INDir').prop('disabled', true);
    }else{
        $('#national-container').prop('hidden', true);
        $('.NDir').prop('disabled', true);
        $('#international-container').prop('hidden', false);
        $('.INDir').prop('disabled', false);
    }
}
function CleanDirectionsFields ()
{
    $('.NDir').val('').trigger('change');
    $('.INDir').val('').trigger('change');
}
/*--------------------ACCIONES MANTENIMIENTO--------------------*/
function Load_Table_MASTER (target, table, child)
{
    $('#table_MASTER').DataTable({
        processing: false,
        ordering  : true,
        select    : false,
        destroy   : true,
        bInfo     : false,
        responsive: true,
        language  : {"url": dataTableLang},
        order     : [0, 'asc'],
        "columnDefs": [
            {
                "targets": [1,2,3,4],
                "orderable": false
            }
        ],
        pageLength : 5,
        lengthMenu : [5, 10, 20, 25],
        ajax: 
        {
            url    : "/PARAMETERS/Records",
            type   : "POST",
            data   : {table:table},
            dataSrc: function(response) 
            {
                var data = response.data;
                if(target == 3){
                    Array_prices = data;
                    for (var i = 0; i < Array_prices.length; i++) {
                        Array_prices[i].price                  = AmountFormat(Array_prices[i].price);
                        Array_prices[i].input_price            = AmountFormat(Array_prices[i].input_price);
                        Array_prices[i].disincorporation_price = AmountFormat(Array_prices[i].disincorporation_price);
                    }
                }
                return data;
            }
        },
        columns: [
            {data: 'description', className: 'text-center'  , defaultContent: ''},
            {data: target == 3 ? 'price' : null                  , title: target == 3 ? 'Tasa de Registro (PTR)' : '' , className: 'text-center' , visible: target == 3 ? true : false , defaultContent: ''},
            {data: target == 3 ? 'input_price' : null            , title: target == 3 ? 'Aporte (PTR)' : ''           , className: 'text-center' , visible: target == 3 ? true : false , defaultContent: ''},
            {data: target == 3 ? 'disincorporation_price' : null , title: target == 3 ? 'Desincorporación (PTR)' : '' , className: 'text-center' , visible: target == 3 ? true : false , defaultContent: ''},
            {
                data: null,
                searchable: false,
                className: "text-center btn-admin",
                render: function (data, type, row, index) {
                    var btn = "";
                    if(Actions.length > 0 && GetActions('Edit') != 'hidden'){
                        btn += '<div class="btn-group">';
                        btn += '<button class="btn btn-secondary btn-admin btn-lg dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">';
                        btn += '<i class="fas fa-cog"></i>';
                        btn += '</button>';
                        btn += '<ul class="dropdown-menu">';
                        if(data.status == true && target != 0){
                            switch(target){
                                case 2:
                                    btn += '<li><a href="#" onClick="Add(' + data.id + ',' + target + ',\'' + child + '\',\''+ data.description +'\');" class="dropdown-item"'+ GetActions('Create') +'>Agregar Descripción</a></li>';
                                    break;
                                case 3:
                                    btn += '<li><a href="#" onClick="EditPrice(' + data.id + ',' + index.row + ');" class="dropdown-item"'+ GetActions('Edit') +'>Editar Tasa</a></li>';
                                break;
                                default:
                                    btn += '<li><a href="#" onClick="Add(' + data.id + ',' + target + ',\'' + child + '\',\''+ data.description +'\');" class="dropdown-item"'+ GetActions('Create') +'>Agregar Descripción</a></li>';
                                    btn += '<li><a href="#" onClick="Edit(' + data.id +',\''+ data.description +'\');" class="dropdown-item"'+ GetActions('Edit') +'>Editar</a></li>';
                                    btn += '<li><a href="#" onClick="ChangeStatus(' + data.id + ',' + 1 + ',' + false + ',\'' + table + '\');" class="dropdown-item" '+ GetActions('Edit') +'>Eliminar</a></li>';
                                break;
                            }
                        }else{
                            btn += '<li><a href="#" onClick="ChangeStatus(' + data.id + ',' + 1 + ',' + true + ',\'' + table + '\');" class="dropdown-item"'+ GetActions('Edit') +'>Habilitar</a></li>';
                        }
                        btn += '</ul>';
                        btn += '</div>';
                    }
                    return btn;
                }
            }
        ]
    });
}
function Load_Table_CHILDRENS (value, target, table)
{
    $('#table_CHILDRENS').DataTable({
        processing : false,
        ordering   : true,
        select     : false,
        destroy    : true,
        bInfo      : false,
        responsive : true,
        language   : {"url": dataTableLang},
        order      : [0, 'asc'],
        "columnDefs": [
            {
                "targets": [1],
                "orderable": false
            }
        ],
        data       : value,
        pageLength : target == 1 ? 5 : 3,
        lengthMenu : target == 1 ? [5, 10, 20, 25] : [3, 5, 10, 20, 25],
        columns    : [
            {data: 'description'      , className: 'text-center', defaultContent: null},
            {
                data      : null,
                searchable: false,
                className : "text-center btn-admin",
                render: function (data, type, row, index) {
                    var btn = "";
                    var smalldsc = table == "collections" ? data.small_description : "";
                    if(data.status == true){
                        btn += '<a onClick="EditChildren(' + data.id +',\''+ data.description +'\',\'' + smalldsc + '\');" class="btn btn-outline-primary btn-sm btn-icon" title="Editar" >\n'+'<i class="far fa-edit"></i>\n'+'</a>';
                        btn += '<a onClick="ChangeStatus(' + data.id + ',' + 2 + ',' + false + ',\'' + table + '\');" class="btn btn-outline-danger btn-sm btn-icon" title="Deshabilitar" >\n'+'<i class="far fa-trash-alt"></i>\n'+'</a>';
                    }else{
                        btn += '<a onClick="ChangeStatus(' + data.id + ',' + 2 + ',' + true + ',\'' + table + '\');" class="btn btn-outline-warning btn-sm btn-icon" title="Habilitar" >\n'+'<i class="fas fa-history"></i>\n'+'</a>';
                    }
                    return btn;
                }
            }
        ]
    });
}
function Create (target) 
{
    $('#id').remove();
    $("#MaintenanceModalLabel").html("<i class='fas fa-plus-square'></i> "+ target == 1 ? "Crear maestro" : "Crear proceso");
    $("#MaintenanceModal").modal("show");
}
function Add (id, target, table, description) 
{
    $("#MaintenanceDscModalLabel").html("<i class='fas fa-plus-square'></i>"+description);
    $('#id').remove();
    $('#id_parent').remove();
    $('<input>').attr({type:'hidden', id:'id_parent', name:'id_parent', value: ''+ id +'', class:'Parent'}).appendTo('form');
    GetDataChildrens(id, target, table);
    if(target != 2){
        switch (id){
            case 1:
                $('#frmContent').removeClass('row-cols-md-1').addClass('row-cols-md-2');
                $('#AltContent').empty().append('<label>Código del documento</label><span class="required-label">*</span><input type="text" id="code" name="code" class="form-control col-sm-6 alpha required AltInput" minlength="1" maxlength="1"/>').prop('hidden', false);
            break;
            case 3:
                $('#frmContent').removeClass('row-cols-md-1').addClass('row-cols-md-2');
                $('#AltContent').empty().append('<label>Código del banco</label><span class="required-label">*</span><input type="text" id="code" name="code" class="form-control col-sm-6 numeric required AltInput" minlength="4" maxlength="4"/>').prop('hidden', false);
            break;
            case 5:
                $('#frmContent').removeClass('row-cols-md-1').addClass('row-cols-md-2');
                $('#AltContent').empty().append('<label>Código de la moneda</label><span class="required-label">*</span><input type="text" id="code" name="code" class="form-control col-sm-6 alphanum required AltInput"/>').prop('hidden', false);
                // $('#AltContent2').empty().append('<label>Pais de de la moneda</label><span class="required-label">*</span><select id="country_coin" name="country_coin" class="form-select select2" required="true"><option value="" selected>Seleccione...</option></select>').prop('hidden', false);
                // FillSelect('country_coin', 'country');
            break;
            case 14:
                $('#frmContent').removeClass('row-cols-md-1').addClass('row-cols-md-2');
                $('#AltContent').empty().append('<label>Código de la Versión</label><span class="required-label">*</span><input type="text" id="code" name="code" class="form-control col-sm-6 alphanum required AltInput"/>').prop('hidden', false);
            break;
            default: 
                $('#frmContent').removeClass('row-cols-md-2').addClass('row-cols-md-1');
                $('#AltContent').empty().prop('hidden', true);
                $('#AltContent2').empty().prop('hidden', true);
            break;
        }
        ApplyFormats();
    }else{
        $('#PrincipalContent').empty().append('<label>Nombre del Recaudo</label><span class="required-label">*</span><input type="text" id="small_description" name="small_description" class="form-control alphanum required">').removeClass('col mb-2').addClass('col-md-12 mb-2');
        $('#SmallContent').empty().append('<label>Descripción</label><span class="required-label">*</span><textarea  type="textarea" id="description" name="description" class="form-control alphaSpecial required" onkeypress="TextAreaCollapse(this)"/>').removeClass('col mb-2').addClass('col-md-12 mb-2').prop('hidden', false);
        ApplyFormats();
        $('#frmContent').removeClass('row-cols-md-2').addClass('row-cols-md-1');
        $('#AltContent').empty().prop('hidden', true);
        $('#AltContent2').empty().prop('hidden', true);
    }
    $("#MaintenanceDscModal").modal("show");
}
function GetDataChildrens (id, target, table)
{
    $.ajax({
        url    : "/PARAMETERS/Records",
        type   : "POST",
        data   : {table:table, id_tb_table:id},
    })
    .done(function(response){
        MASTER_helper = response.data;
        Load_Table_CHILDRENS(response.data, target, table);
    })
    .fail(function(jqXHR, textStatus, errorThrown) {
        Swal.fire(jqXHR.status.toString(), jqXHR.responseJSON.error, 'error');
    });
}
function Edit (id, description, JSONDetail) 
{
    $("#MaintenanceModalLabel").html("<i class='fas fa-edit'></i> Editar "+description);
    $('#id').remove();
    $('<input>').attr({type:'hidden', id:'id', name:'id', value: ''+ id +''}).appendTo('form');
    $('#MasterDsc').val(description);
    $("#MaintenanceModal").modal("show");
}
function EditChildren (id, description, smalldsc) 
{
    $('.Children').remove();
    $('<input>').attr({type:'hidden', id:'id', name:'id', value: ''+ id +'', class:'Children'}).appendTo('form');
    if($('.AltInput').length != 0){
        description = description.split('-');
        $('#code').val(description[0].trim());
        $('#description').val(description[1].trim());
    }else{
        $('#description').val(description);
    }
    if(smalldsc != ""){
        $('#small_description').val(smalldsc);
    }
}
function SaveMaintenance (form, table, modal) 
{
    if($('#'+form).valid() == true){
        $.ajax({
            url  : '/MASTER/StoreMaintenance',
            type : 'POST',
            data : $('#'+form).serialize() + '&table=' + table
        })
        .done(function(response){
            if(response.code == 200){
                Swal.fire(response.message, '', 'success')
                .then(() => {
                    switch (table) {
                        case 'module':
                            Load_Table_MASTER(3, 'module', 'collections');
                        break;
                        case 'collections':
                            Load_Table_MASTER(2, 'module', 'collections');
                        break;
                        default:
                            Load_Table_MASTER(1, 'tb_table', 'tb_master');
                        break;
                    }
                    clean_modals(form);
                    $('#'+modal).modal('toggle');
                });
            }else{
                toastr.error(response.message);
            }
        })
        .fail(function(jqXHR, textStatus, errorThrown) {
            Swal.fire(jqXHR.status.toString(), jqXHR.responseJSON.error, 'error');
        });
    }
}
function ChangeStatus (id, target, status, table) 
{
    $.ajax({
        url  : '/PARAMETERS/ChangeStatus',
        type : 'POST',
        data : {id:id, status: status, table:table}
    })
    .done(function(response){
        if(response.code == 200){
            Swal.fire(response.message, '', 'success')
            .then(() => {
                if(target != 1){
                    GetDataChildrens($('#id_parent').val(), table != 'collections' ? 1 : 2, table);
                }else{
                    Load_Table_MASTER(table != 'collections' ? 1 : 2, table != 'collections' ? 'tb_table' : 'collections', table != 'collections' ? 'tb_master' : 'collections');
                }
            });
        }
    })
    .fail(function(jqXHR, textStatus, errorThrown) {
        Swal.fire(jqXHR.status.toString(), jqXHR.responseJSON.error, 'error');
    });
}
function StoreDrop (input, table, target)
{
    var childrens = '';
    $('#drop > div').each(function () {
        childrens += $(this).attr('id')+',';     
    });
    childrens = childrens.slice(0,-1);
    $.ajax({
        url  : '/PARAMETERS/StoreDrop',
        type : 'POST',
        data : {id:$('#id').val(), table:table, childrens:childrens}
    })
    .done(function(response){
        if(response.code == 200){
            Swal.fire(response.message, '', 'success')
                .then(() => {
                    $('#'+target).modal('toggle');
                    CleanDrag();
                });
        }
    })
    .fail(function(jqXHR, textStatus, errorThrown) {
        Swal.fire(jqXHR.status.toString(), jqXHR.responseJSON.error, 'error');
    });
}
/*--------------------MANEJO DE PERMISOS--------------------*/
function AddPermits (id, type, id_group)
{
    var StrCheck = 
    "<div class='permisions row'>"+
        "<div class='col border-top'>"+
            "<div class='form-check'>"+
                " <input class='form-check-input SpecialPermit' type='checkbox' value='View' id='view' disabled>"+
                " <label class='form-check-label' for='view'>"+
                "Visualizar"+
                "</label>"+
            "</div>"+
        "</div>"+
        "<div class='col border-top'>"+
            "<div class='form-check'>"+
                " <input class='form-check-input SpecialPermit' type='checkbox' value='Create' id='create' disabled>"+
                " <label class='form-check-label' for='create'>"+
                "Crear"+
                "</label>"+
            "</div>"+
        "</div>"+
        "<div class='col border-top'>"+
            "<div class='form-check'>"+
                " <input class='form-check-input SpecialPermit' type='checkbox' value='Edit' id='edit' disabled>"+
                " <label class='form-check-label' for='edit'>"+
                "Editar"+
                "</label>"+
            "</div>"+
        "</div>"+
    "</div>"
    $.ajax({
        url  : '/USERS/GetPermits',
        type : 'POST',
        data : {id:id, table: type == 1 ? 'groups' : 'users', id_group: id_group == 0 ? '' : id_group},
    })
    .done(function(response){
        var Array_available = response.available;
        var Array_assigned  = response.assigned;
        for (var j = 0; j < Array_available.length; j++) {
            $('#pool').append("<div id='card"+ Array_available[j].id_submodule +"' data-module='"+ Array_available[j].id_module +"' data-submodule='"+ Array_available[j].id_submodule +"' class='box-drop'><input type='checkbox' class='boxi-toggle'><i class='fas fa-chevron-down icon-boxi'></i><div class='name'>"+ Array_available[j].module_description + ": " + Array_available[j].submodule_description +"</div>"+StrCheck+"</div>");
        }
        if(Array_assigned.length > 0){
            for (var i = 0; i < Array_assigned.length; i++) {
                var ChkView   = Array_assigned[i].actions.indexOf("View")   >= 0 ? 'checked' : '';
                var ChkCreate = Array_assigned[i].actions.indexOf("Create") >= 0 ? 'checked' : '';
                var ChkEdit   = Array_assigned[i].actions.indexOf("Edit")   >= 0 ? 'checked' : '';
                var StrCheck2 = 
                "<div class='permisions row'>"+
                    "<div class='col border-top'>"+
                        "<div class='form-check'>"+
                            " <input class='form-check-input SpecialPermit ChckAll' type='checkbox' value='View' id='view' " + ChkView + ">"+
                            " <label class='form-check-label' for='view'>"+
                            "Visualizar"+
                            "</label>"+
                        "</div>"+
                    "</div>"+
                    "<div class='col border-top'>"+
                        "<div class='form-check'>"+
                            " <input class='form-check-input SpecialPermit ChckAll' type='checkbox' value='Create' id='create' " + ChkCreate + ">"+
                            " <label class='form-check-label' for='create'>"+
                            "Crear"+
                            "</label>"+
                        "</div>"+
                    "</div>"+
                    "<div class='col border-top'>"+
                        "<div class='form-check'>"+
                            " <input class='form-check-input SpecialPermit ChckAll' type='checkbox' value='Edit' id='edit' " + ChkEdit + ">"+
                            " <label class='form-check-label' for='edit'>"+
                            "Editar"+
                            "</label>"+
                        "</div>"+
                    "</div>"+
                "</div>"
                $('#drop').append("<div id='card"+ Array_assigned[i].id_submodule +"' data-module='"+ Array_assigned[i].id_module +"' data-submodule='"+ Array_assigned[i].id_submodule +"' class='box-drop'><input type='checkbox' class='boxi-toggle'><i class='fas fa-chevron-down icon-boxi'></i><div class='name'>"+ Array_assigned[i].module_description + ": " + Array_assigned[i].submodule_description +"</div>"+StrCheck2+"</div>");
                Array_assigned[i].actions.indexOf("View")   >= 0 ? $('#card'+Array_assigned[i].id_submodule).attr('data-view'  , 'View')   : '';
                Array_assigned[i].actions.indexOf("Create") >= 0 ? $('#card'+Array_assigned[i].id_submodule).attr('data-edit'  , 'Edit')   : '';
                Array_assigned[i].actions.indexOf("Edit")   >= 0 ? $('#card'+Array_assigned[i].id_submodule).attr('data-create', 'Create') : '';
            }
        }
        $('.SpecialPermit').on('change', function() {
            if($(this).prop('checked') == true ){
                $(this).parent().parent().parent().parent().attr('data-'+$(this).attr('id'), $(this).val());
            }else{
                $(this).parent().parent().parent().parent().removeAttr('data-'+$(this).attr('id'));
            }
            AllChecked();
        });
        $('#CheckAll').on('click', function () {
            if($('#CheckAll').is(':checked') == true){
                $('.ChckAll').prop('checked', true).trigger('change');
            }else{
                $('.ChckAll').prop('checked', false).trigger('change');
            }
        });
        AllChecked();
        ApplyFormats();
        $('#id').remove();
        $('<input>').attr({type:'hidden', id:'id', name:'id', value: ''+ id +''}).appendTo('#drag-content');
        $("#PermitsModalLabel").html("<i class='icon left fas fa-list'></i> "+"Asignación de Permisos");
        $("#PermitsModal").modal("show");
    })
    .fail(function(jqXHR, textStatus, errorThrown) {
        Swal.fire(jqXHR.status.toString(), jqXHR.responseJSON.error, 'error');
    });
}
function StorePermits (type)
{
    Array_permits = [];
    $('#drop > div').each(function () {
        var view          = $(this).attr('data-view')     == undefined ? 'null' : $(this).attr('data-view');
        var create        = $(this).attr('data-create')   == undefined ? 'null' : $(this).attr('data-create');
        var edit          = $(this).attr('data-edit')     == undefined ? 'null' : $(this).attr('data-edit');
        var array_actions = view + ',' + create + ',' + edit;
        var actions       = '';
        array_actions     = array_actions.split(',');
        for (var i = 0; i < array_actions.length; i++) {
            if(array_actions[i] == 'null'){
                array_actions.splice(i, 1);
            }else{
                actions += array_actions[i] + ','
            }
        }
        var obj_permits = {
            "id"          : $('#id').val(),
            "id_module"   : $(this).attr('data-module'),
            "id_submodule": $(this).attr('data-submodule'),
            "actions"     : actions.slice(0, -1)
        }  
        Array_permits.push(obj_permits);
    });
    if(ValidatePermits() == true){
        $.ajax({
            url : "/USERS/StorePermits",
            type: 'POST',
            data: {id: $('#id').val(), permits: JSON.stringify(Array_permits)}
        })
        .done(function(response) {
            if (response.code == 200) {                            
                Swal.fire('Registro exitoso','Los cambios serán efectivos en el proximo inicio de sesión','success')
                    .then(() => {
                        $('#PermitsModal').modal('toggle');
                        CleanDrag();
                    });
            }else{
                toastr.error(response.message);
            }
        })
        .fail(function(jqXHR, textStatus, errorThrown) {
            Swal.fire(jqXHR.status.toString(), jqXHR.responseJSON.error, 'error');
        });
    }
}
function ValidatePermits ()
{
    if(Array_permits.length > 0){
        for (var i = 0; i < Array_permits.length; i++) {
            if(Array_permits[i].actions == ''){
                toastr.error("Debe seleccionar almenos una acción", 'Error: ');
                return false;
            }
        }
    }
    return true;
}
function AllChecked ()
{
    if($('#drop').find('input[type=checkbox].ChckAll:checked').length == $('.ChckAll').length && $('.ChckAll').length > 0){
        $('#CheckAll').prop('checked', true);
    }else{
        $('#CheckAll').prop('checked', false);
    }
}

function AmountFormat (amount) 
{
    amount = parseFloat(amount);
    var amount_formated = amount.toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,');
    amount_formated = amount_formated.toString();
    amount_formated = amount_formated.replace('.', '|');
    amount_formated = amount_formated.replace(/[,]/g, '.');
    amount_formated = amount_formated.replace(/[|]/g, ',');
    return amount_formated;
}

function AccountMask(account) {
    if(account != ''){
        return account.substring(0, 4)+"*****"+account.substring(account.length - 4, account.length);
    }else{
        return '';
    }
}