/**
* bootstrap-formhelpers.js v2.3.0 by @vincentlamanna
* Copyright 2013 Vincent Lamanna
* http://www.apache.org/licenses/LICENSE-2.0
*/
if (!jQuery) { throw new Error("Bootstrap Form Helpers requires jQuery"); }

/* ==========================================================
 * bootstrap-formhelpers-datepicker.en_US.js
 * https://github.com/vlamanna/BootstrapFormHelpers
 * ==========================================================
 * Copyright 2012 Vincent Lamanna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================== */
 
var BFHMonthsList = [
  'Enero',
  'Febrero',
  'Marzo',
  'Abril',
  'Mayo',
  'Junio',
  'Julio',
  'Agosto',
  'Septiembre',
  'Octubre',
  'Noviembre',
  'Diciembre'
];
 
var BFHDaysList = [
  'DOM',
  'LUN',
  'MAR',
  'MIE',
  'JUE',
  'VIE',
  'SAB'
];

var BFHDayOfWeekStart = 0;

/* ==========================================================
 * bootstrap-formhelpers-timepicker.en_US.js
 * https://github.com/vlamanna/BootstrapFormHelpers
 * ==========================================================
 * Copyright 2012 Vincent Lamanna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================== */
 
var BFHTimePickerDelimiter = ':';

var BFHTimePickerModes = {
  'am': 'AM',
  'pm': 'PM'
};

/* ==========================================================
 * bootstrap-formhelpers-datepicker.js
 * https://github.com/vlamanna/BootstrapFormHelpers
 * ==========================================================
 * Copyright 2012 Vincent Lamanna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================== */

+function ($) {

  'use strict';


  /* BFHDATEPICKER CLASS DEFINITION
   * ========================= */

  var toggle = '[data-toggle=bfh-datepicker]',
      BFHDatePicker = function (element, options) {
        this.options = $.extend({}, $.fn.bfhdatepicker.defaults, options);
        this.$element = $(element);

        this.initCalendar();
      };

  BFHDatePicker.prototype = {

    constructor: BFHDatePicker,

    setDate: function() {
      var date,
          today,
          format;

      date = this.options.date;
      format = this.options.format;

      if (date === '' || date === 'today' || date === undefined) {
        today = new Date();

        if (date === 'today') {
          this.$element.val(formatDate(format, today.getMonth(), today.getFullYear(), today.getDate()));
        }

        this.$element.data('month', today.getMonth());
        this.$element.data('year', today.getFullYear());
      } else {
        this.$element.val(date);
        this.$element.data('month', Number(getDatePart(format, date, 'm') - 1));
        this.$element.data('year', Number(getDatePart(format, date, 'y')));
      }
    },

    setDateLimit: function(date, limitPrefix) {
      var today,
          format;

      format = this.options.format;

      if (date !== '') {
        this.$element.data(limitPrefix + 'limit', true);

        if (date === 'today') {
          today = new Date();

          this.$element.data(limitPrefix + 'day', today.getDate());
          this.$element.data(limitPrefix + 'month', today.getMonth());
          this.$element.data(limitPrefix + 'year', today.getFullYear());
        } else {
          this.$element.data(limitPrefix + 'day', Number(getDatePart(format, date, 'd')));
          this.$element.data(limitPrefix + 'month', Number(getDatePart(format, date, 'm') - 1));
          this.$element.data(limitPrefix + 'year', Number(getDatePart(format, date, 'y')));
        }
      } else {
        this.$element.data(limitPrefix + 'limit', false);
      }
    },

    initCalendar: function() {
    	/*
      var iconLeft,
          iconRight,
          iconAddon;

      iconLeft = '';
      iconRight = '';
      iconAddon = '';
      if (this.options.icon !== '') {
        if (this.options.align === 'right') {
          iconRight = '<span class="input-group-addon"><i class="' + this.options.icon + '"></i></span>';
        } else {
          iconLeft = '<span class="input-group-addon"><i class="' + this.options.icon + '"></i></span>';
        }
        iconAddon = 'input-group';
      }

      this.$element.html(
        '<div class="' + iconAddon + ' bfh-datepicker-toggle" data-toggle="bfh-datepicker">' +
        iconLeft +
        '<input type="text" name="' + this.options.name + '" class="' + this.options.input + '" placeholder="' + this.options.placeholder + '" readonly>' +
        iconRight +
        '</div>' +
        '<div class="bfh-datepicker-calendar">' +
        '<table class="calendar table table-bordered">' +
        '<thead>' +
        '<tr class="months-header">' +
        '<th class="month" colspan="4">' +
        '<a class="previous" href="#"><i class="glyphicon glyphicon-chevron-left"></i></a>' +
        '<span></span>' +
        '<a class="next" href="#"><i class="glyphicon glyphicon-chevron-right"></i></a>' +
        '</th>' +
        '<th class="year" colspan="3">' +
        '<a class="previous" href="#"><i class="glyphicon glyphicon-chevron-left"></i></a>' +
        '<span></span>' +
        '<a class="next" href="#"><i class="glyphicon glyphicon-chevron-right"></i></a>' +
        '</th>' +
        '</tr>' +
        '<tr class="days-header">' +
        '</tr>' +
        '</thead>' +
        '<tbody>' +
        '</tbody>' +
        '</table>' +
        '</div>'
      );
    	 	*/
      this.$element
        .on('click.bfhdatepicker.data-api touchstart.bfhdatepicker.data-api', toggle, BFHDatePicker.prototype.toggle)
        .on('click.bfhdatepicker.data-api touchstart.bfhdatepicker.data-api', '.bfh-datepicker-calendar > table.calendar .month > .previous', BFHDatePicker.prototype.previousMonth)
        .on('click.bfhdatepicker.data-api touchstart.bfhdatepicker.data-api', '.bfh-datepicker-calendar > table.calendar .month > .next', BFHDatePicker.prototype.nextMonth)
        .on('click.bfhdatepicker.data-api touchstart.bfhdatepicker.data-api', '.bfh-datepicker-calendar > table.calendar .year > .previous', BFHDatePicker.prototype.previousYear)
        .on('click.bfhdatepicker.data-api touchstart.bfhdatepicker.data-api', '.bfh-datepicker-calendar > table.calendar .year > .next', BFHDatePicker.prototype.nextYear)
        .on('click.bfhdatepicker.data-api touchstart.bfhdatepicker.data-api', '.bfh-datepicker-calendar > table.calendar td:not(.off)', BFHDatePicker.prototype.select)
        .on('click.bfhdatepicker.data-api touchstart.bfhdatepicker.data-api', '.bfh-datepicker-calendar > table.calendar', function() { return false; });

      this.setDate();
      this.setDateLimit(this.options.min, 'lower');
      this.setDateLimit(this.options.max, 'higher');

      this.updateCalendar();
    },

    updateCalendarHeader: function($calendar, month, year) {
      var $daysHeader,
          day;

      $calendar.find('table > thead > tr > th.month > span').text(BFHMonthsList[month]);
      $calendar.find('table > thead > tr > th.year > span').text(year);

      $daysHeader = $calendar.find('table > thead > tr.days-header');
      $daysHeader.html('');
      for (day=BFHDayOfWeekStart; day < BFHDaysList.length; day=day+1) {
        $daysHeader.append('<th>' + BFHDaysList[day] + '</th>');
      }
      for (day=0; day < BFHDayOfWeekStart; day=day+1) {
        $daysHeader.append('<th>' + BFHDaysList[day] + '</th>');
      }
    },

    checkMinDate: function(day, month, year) {
      var lowerlimit,
          lowerday,
          lowermonth,
          loweryear;

      lowerlimit = this.$element.data('lowerlimit');

      if (lowerlimit === true) {
        lowerday = this.$element.data('lowerday');
        lowermonth = this.$element.data('lowermonth');
        loweryear = this.$element.data('loweryear');

        if ((day < lowerday && month === lowermonth && year === loweryear) || (month < lowermonth && year === loweryear) || (year < loweryear)) {
          return true;
        }
      }

      return false;
    },

    checkMaxDate: function(day, month, year) {
      var higherlimit,
          higherday,
          highermonth,
          higheryear;

      higherlimit = this.$element.data('higherlimit');

      if (higherlimit === true) {
        higherday = this.$element.data('higherday');
        highermonth = this.$element.data('highermonth');
        higheryear = this.$element.data('higheryear');

        if ((day > higherday && month === highermonth && year === higheryear) || (month > highermonth && year === higheryear) || (year > higheryear)) {
          return true;
        }
      }

      return false;
    },

    checkToday: function(day, month, year) {
      var today;

      today = new Date();

      if (day === today.getDate() && month === today.getMonth() && year === today.getFullYear()) {
        return true;
      }

      return false;
    },

    updateCalendarDays: function($calendar, month, year) {
      var $days,
          numDaysPreviousMonth,
          numDaysCurrentMonth,
          firstDay,
          lastDay,
          row,
          day;

      $days = $calendar.find('table > tbody').html('');
      numDaysPreviousMonth = getNumDaysInMonth(month, year);
      numDaysCurrentMonth = getNumDaysInMonth(month + 1, year);
      firstDay = getDayOfWeek(month, year, 1);
      lastDay = getDayOfWeek(month, year, numDaysCurrentMonth);

      row = '';
      for (day=0; day < (firstDay - BFHDayOfWeekStart + 7) % 7; day=day+1) {
        row += '<td class="off">' + (numDaysPreviousMonth - (firstDay - BFHDayOfWeekStart + 7) % 7 + day + 1) + '</td>';
      }

      for (day=1; day <= numDaysCurrentMonth; day=day+1) {
        if (this.checkMinDate(day, month, year)) {
          row += '<td data-day="' + day + '" class="off">' + day + '</td>';
        } else if (this.checkMaxDate(day, month, year)) {
          row += '<td data-day="' + day + '" class="off">' + day + '</td>';
        } else if (this.checkToday(day, month, year)) {
          row += '<td data-day="' + day + '" class="today">' + day + '</td>';
        } else {
          row += '<td data-day="' + day + '">' + day + '</td>';
        }
        if (getDayOfWeek(month, year, day) === (6 + BFHDayOfWeekStart) % 7) {
          $days.append('<tr>' + row + '</tr>');
          row = '';
        }
      }

      for (day=1; day <= (7 - ((lastDay + 1 - BFHDayOfWeekStart + 7) % 7)) % 7 + 1; day=day+1) {
        row += '<td class="off">' + day + '</td>';
        if (day === (7 - ((lastDay + 1 - BFHDayOfWeekStart + 7) % 7)) % 7) {
          $days.append('<tr>' + row + '</tr>');
        }
      }
    },

    updateCalendar: function () {
      var $calendar,
          month,
          year;

      $calendar = this.$element.find('.bfh-datepicker-calendar');
      month = this.$element.data('month');
      year = this.$element.data('year');

      this.updateCalendarHeader($calendar, month, year);
      this.updateCalendarDays($calendar, month, year);
    },

    previousMonth: function () {
      var $this,
          $parent,
          $datePicker;

      $this = $(this);
      $parent = getParent($this);

      if (Number($parent.data('month')) === 0) {
        $parent.data('month', 11);
        $parent.data('year', Number($parent.data('year')) - 1);
      } else {
        $parent.data('month', Number($parent.data('month')) - 1);
      }

      $datePicker = $parent.data('bfhdatepicker');
      $datePicker.updateCalendar();

      return false;
    },

    nextMonth: function () {
      var $this,
          $parent,
          $datePicker;

      $this = $(this);
      $parent = getParent($this);

      if (Number($parent.data('month')) === 11) {
        $parent.data('month', 0);
        $parent.data('year', Number($parent.data('year')) + 1);
      } else {
        $parent.data('month', Number($parent.data('month')) + 1);
      }

      $datePicker = $parent.data('bfhdatepicker');
      $datePicker.updateCalendar();

      return false;
    },

    previousYear: function () {
      var $this,
          $parent,
          $datePicker;

      $this = $(this);
      $parent = getParent($this);

      $parent.data('year', Number($parent.data('year')) - 1);

      $datePicker = $parent.data('bfhdatepicker');
      $datePicker.updateCalendar();

      return false;
    },

    nextYear: function () {
      var $this,
          $parent,
          $datePicker;

      $this = $(this);
      $parent = getParent($this);

      $parent.data('year', Number($parent.data('year')) + 1);

      $datePicker = $parent.data('bfhdatepicker');
      $datePicker.updateCalendar();

      return false;
    },

    select: function (e) {
      var $this,
          $parent,
          $datePicker,
          month,
          year,
          day;

      $this = $(this);

      e.preventDefault();
      e.stopPropagation();

      $parent = getParent($this);
      $datePicker = $parent.data('bfhdatepicker');
      month = $parent.data('month');
      year = $parent.data('year');
      day = $this.data('day');

      $parent.val(formatDate($datePicker.options.format, month, year, day));
      $parent.trigger('change.bfhdatepicker');

      if ($datePicker.options.close === true) {
        clearMenus();
      }
    },

    toggle: function (e) {
      var $this,
          $parent,
          isActive;

      $this = $(this);
      $parent = getParent($this);

      if ($parent.is('.disabled') || $parent.attr('disabled') !== undefined) {
        return true;
      }

      isActive = $parent.hasClass('open');

      clearMenus();

      if (!isActive) {
        $parent.trigger(e = $.Event('show.bfhdatepicker'));

        if (e.isDefaultPrevented()) {
          return true;
        }

        $parent
          .toggleClass('open')
          .trigger('shown.bfhdatepicker');

        $this.focus();
      }

      return false;
    }
  };

  function getNumDaysInMonth(month, year) {
    return new Date(year, month, 0).getDate();
  }

  function getDayOfWeek(month, year, day) {
    return new Date(year, month, day).getDay();
  }

  function formatDate(format, month, year, day) {
    month += 1;
    month = String(month);
    day = String(day);

    if (month.length === 1) {
      month = '0' + month;
    }
    if (day.length === 1) {
      day = '0' + day;
    }

    return format.replace('m', month).replace('y', year).replace('d', day);
  }

  function getDatePart(format, date, part) {
    var partPositions,
        partPosition,
        parts;

    partPositions = [
      {'part': 'm', 'position': format.indexOf('m')},
      {'part': 'y', 'position': format.indexOf('y')},
      {'part': 'd', 'position': format.indexOf('d')}
    ];

    partPositions.sort(function(a, b) {return a.position - b.position;});

    parts = date.match(/(\d+)/g);

    for (partPosition in partPositions) {
      if (partPositions.hasOwnProperty(partPosition)) {
        if (partPositions[partPosition].part === part) {
          return Number(parts[partPosition]).toString();
        }
      }
    }
  }

  function clearMenus() {
    var $parent;

    $(toggle).each(function (e) {
      $parent = getParent($(this));

      if (!$parent.hasClass('open')) {
        return true;
      }

      $parent.trigger(e = $.Event('hide.bfhdatepicker'));

      if (e.isDefaultPrevented()) {
        return true;
      }

      $parent
        .removeClass('open')
        .trigger('hidden.bfhdatepicker');
    });
  }

  function getParent($this) {
    return $this.closest('.bfh-datepicker');
  }


  /* DATEPICKER PLUGIN DEFINITION
   * ========================== */

  var old = $.fn.bfhdatepicker;

  $.fn.bfhdatepicker = function (option) {
    return this.each(function () {
      var $this,
          data,
          options;

      $this = $(this);
      data = $this.data('bfhdatepicker');
      options = typeof option === 'object' && option;
      this.type = 'bfhdatepicker';

      if (!data) {
        $this.data('bfhdatepicker', (data = new BFHDatePicker(this, options)));
      }
      if (typeof option === 'string') {
        data[option].call($this);
      }
    });
  };

  $.fn.bfhdatepicker.Constructor = BFHDatePicker;

  $.fn.bfhdatepicker.defaults = {
    icon: 'glyphicon glyphicon-calendar',
    align: 'left',
    input: 'form-control',
    placeholder: '',
    name: '',
    date: 'today',
    format: 'm/d/y',
    min: '',
    max: '',
    close: true
  };


  /* DATEPICKER NO CONFLICT
   * ========================== */

  $.fn.bfhdatepicker.noConflict = function () {
    $.fn.bfhdatepicker = old;
    return this;
  };


  /* DATEPICKER VALHOOKS
   * ========================== */

  var origHook;
  if ($.valHooks.div){
    origHook = $.valHooks.div;
  }
  $.valHooks.div = {
    get: function(el) {
      if ($(el).hasClass('bfh-datepicker')) {
        return $(el).find('input[type="text"]').val();
      } else if (origHook) {
        return origHook.get(el);
      }
    },
    set: function(el, val) {
      if ($(el).hasClass('bfh-datepicker')) {
        $(el).find('input[type="text"]').val(val);
      } else if (origHook) {
        return origHook.set(el,val);
      }
    }
  };


  /* DATEPICKER DATA-API
   * ============== */

  $(document).ready( function () {
    $('div.bfh-datepicker').each(function () {
      var $datepicker;

      $datepicker = $(this);

      $datepicker.bfhdatepicker($datepicker.data());
    });
  });


  /* APPLY TO STANDARD DATEPICKER ELEMENTS
   * =================================== */

  $(document)
    .on('click.bfhdatepicker.data-api', clearMenus);

}(window.jQuery);


/* ==========================================================
 * bootstrap-formhelpers-number.js
 * https://github.com/vlamanna/BootstrapFormHelpers
 * ==========================================================
 * Copyright 2012 Vincent Lamanna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================== */

+function ($) {

  'use strict';


  /* NUMBER CLASS DEFINITION
   * ====================== */

  var BFHNumber = function (element, options) {
    this.options = $.extend({}, $.fn.bfhnumber.defaults, options);
    this.$element = $(element);

    this.initInput();
  };

  BFHNumber.prototype = {

    constructor: BFHNumber,

    initInput: function() {
      var value;
      
      if (this.options.buttons === true) {
        this.$element.wrap('<div class="input-group"></div>');
        this.$element.parent().append('<span class="input-group-addon bfh-number-btn inc"><span class="glyphicon glyphicon-chevron-up"></span></span>');
        this.$element.parent().append('<span class="input-group-addon bfh-number-btn dec"><span class="glyphicon glyphicon-chevron-down"></span></span>');
      }
      
      this.$element.on('change.bfhnumber.data-api', BFHNumber.prototype.change);
        
      if (this.options.keyboard === true) {
        this.$element.on('keydown.bfhnumber.data-api', BFHNumber.prototype.keydown);
      }
      
      if (this.options.buttons === true) {
        this.$element.parent()
          .on('mousedown.bfhnumber.data-api', '.inc', BFHNumber.prototype.btninc)
          .on('mousedown.bfhnumber.data-api', '.dec', BFHNumber.prototype.btndec);
      }
      
      this.formatNumber();
    },
    
    keydown: function(e) {
      var $this;
      
      $this = $(this).data('bfhnumber');
      
      if ($this.$element.is('.disabled') || $this.$element.attr('disabled') !== undefined) {
        return true;
      }
      
      switch (e.which) {
        case 38:
          $this.increment();
          break;
        case 40:
          $this.decrement();
          break;
        default:
      }
      
      return true;
    },
    
    mouseup: function(e) {
      var $this,
          timer,
          interval;
      
      $this = e.data.btn;
      timer = $this.$element.data('timer');
      interval = $this.$element.data('interval');
      
      clearTimeout(timer);
      clearInterval(interval);
    },
    
    btninc: function() {
      var $this,
          timer;
      
      $this = $(this).parent().find('.bfh-number').data('bfhnumber');
      
      if ($this.$element.is('.disabled') || $this.$element.attr('disabled') !== undefined) {
        return true;
      }
      
      $this.increment();
      
      timer = setTimeout(function() {
        var interval;
        interval = setInterval(function() {
          $this.increment();
        }, 80);
        $this.$element.data('interval', interval);
      }, 750);
      $this.$element.data('timer', timer);
      
      $(document).one('mouseup', {btn: $this}, BFHNumber.prototype.mouseup);
      
      return true;
    },
    
    btndec: function() {
      var $this,
          timer;
      
      $this = $(this).parent().find('.bfh-number').data('bfhnumber');
      
      if ($this.$element.is('.disabled') || $this.$element.attr('disabled') !== undefined) {
        return true;
      }
      
      $this.decrement();
      
      timer = setTimeout(function() {
        var interval;
        interval = setInterval(function() {
          $this.decrement();
        }, 80);
        $this.$element.data('interval', interval);
      }, 750);
      $this.$element.data('timer', timer);
      
      $(document).one('mouseup', {btn: $this}, BFHNumber.prototype.mouseup);
      
      return true;
    },
    
    change: function() {
      var $this;

      $this = $(this).data('bfhnumber');

      if ($this.$element.is('.disabled') || $this.$element.attr('disabled') !== undefined) {
        return true;
      }

      $this.formatNumber();

      return true;
    },
    
    increment: function() {
      var value;
      
      value = this.getValue();
      
      value = value + 1;
      
      this.$element.val(value).change();
    },
    
    decrement: function() {
      var value;
      
      value = this.getValue();
      
      value = value - 1;
      
      this.$element.val(value).change();
    },
    
    getValue: function() {
      var value;
      
      value = this.$element.val();
      if (value !== '-1') {
        value = String(value).replace(/\D/g, '');
      }
      if (String(value).length === 0) {
        value = this.options.min;
      }
      
      return parseInt(value);
    },
    
    formatNumber: function() {
      var value,
          maxLength,
          length,
          zero;
      
      value = this.getValue();
      
      if (value > this.options.max) {
        if (this.options.wrap === true) {
          value = this.options.min;
        } else {
          value = this.options.max;
        }
      }
      
      if (value < this.options.min) {
        if (this.options.wrap === true) {
          value = this.options.max;
        } else {
          value = this.options.min;
        }
      }
      
      if (this.options.zeros === true) {
        maxLength = String(this.options.max).length;
        length = String(value).length;
        for (zero=length; zero < maxLength; zero = zero + 1) {
          value = '0' + value;
        }
      }
      
      if (value !== this.$element.val()) {
        this.$element.val(value);
      }
    }

  };

  /* NUMBER PLUGIN DEFINITION
   * ======================= */

  var old = $.fn.bfhnumber;

  $.fn.bfhnumber = function (option) {
    return this.each(function () {
      var $this,
          data,
          options;

      $this = $(this);
      data = $this.data('bfhnumber');
      options = typeof option === 'object' && option;

      if (!data) {
        $this.data('bfhnumber', (data = new BFHNumber(this, options)));
      }
      if (typeof option === 'string') {
        data[option].call($this);
      }
    });
  };

  $.fn.bfhnumber.Constructor = BFHNumber;

  $.fn.bfhnumber.defaults = {
    min: 0,
    max: 9999,
    zeros: false,
    keyboard: true,
    buttons: true,
    wrap: false
  };


  /* NUMBER NO CONFLICT
   * ========================== */

  $.fn.bfhnumber.noConflict = function () {
    $.fn.bfhnumber = old;
    return this;
  };


  /* NUMBER DATA-API
   * ============== */

  $(document).ready( function () {
    $('form input[type="text"].bfh-number, form input[type="number"].bfh-number').each(function () {
      var $number;

      $number = $(this);

      $number.bfhnumber($number.data());
    });
  });


  /* APPLY TO STANDARD NUMBER ELEMENTS
   * =================================== */


}(window.jQuery);


/* ==========================================================
 * bootstrap-formhelpers-timepicker.js
 * https://github.com/vlamanna/BootstrapFormHelpers
 * ==========================================================
 * Copyright 2012 Vincent Lamanna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================== */


+function ($) {

  'use strict';


 /* TIMEPICKER CLASS DEFINITION
  * ========================= */

  var toggle = '[data-toggle=bfh-timepicker]',
      BFHTimePicker = function (element, options) {
        this.options = $.extend({}, $.fn.bfhtimepicker.defaults, options);
        this.$element = $(element);

        this.initPopover();
      };

  BFHTimePicker.prototype = {

    constructor: BFHTimePicker,

    setTime: function() {
      var time,
          today,
          timeParts,
          hours,
          minutes,
          mode,
          currentMode;

      time = this.options.time;
      mode = '';
      currentMode = '';
      
      if (time === '' || time === 'now' || time === undefined) {
        today = new Date();

        hours = today.getHours();
        minutes = today.getMinutes();
        
        if (this.options.mode === '12h') {
          if (hours > 12) {
            hours = hours - 12;
            mode = ' ' + BFHTimePickerModes.pm;
            currentMode = 'pm';
          } else {
            mode = ' ' + BFHTimePickerModes.am;
            currentMode = 'am';
          }
        }
        
        if (time === 'now') {
          //this.$element.find('.bfh-timepicker-toggle > input[type="text"]').val(formatTime(hours, minutes) + mode);
        }

        this.$element.data('hour', hours);
        this.$element.data('minute', minutes);
        this.$element.data('mode', currentMode);
      } else {
        timeParts = String(time).split(BFHTimePickerDelimiter);
        hours = timeParts[0];
        minutes = timeParts[1];
        
        if (this.options.mode === '12h') {
          timeParts = String(minutes).split(' ');
          minutes = timeParts[0];
          if (timeParts[1] === BFHTimePickerModes.pm) {
            currentMode = 'pm';
          } else {
            currentMode = 'am';
          }
        }
        
        //this.$element.find('.bfh-timepicker-toggle > input[type="text"]').val(time);
        this.$element.data('hour', hours);
        this.$element.data('minute', minutes);
        this.$element.data('mode', currentMode);
      }
    },

    initPopover: function() {
    	/*
      var iconLeft,
          iconRight,
          iconAddon,
          modeAddon,
          modeMax;

      iconLeft = '';
      iconRight = '';
      iconAddon = '';
      if (this.options.icon !== '') {
        if (this.options.align === 'right') {
          iconRight = '<span class="input-group-addon"><i class="' + this.options.icon + '"></i></span>';
        } else {
          iconLeft = '<span class="input-group-addon"><i class="' + this.options.icon + '"></i></span>';
        }
        iconAddon = 'input-group';
      }
      
      modeAddon = '';
      modeMax = '23';
      if (this.options.mode === '12h') {
        modeAddon = '<td>' +
          '<div class="bfh-selectbox" data-input="' + this.options.input + '" data-value="am">' +
          '<div data-value="am">' + BFHTimePickerModes.am + '</div>' +
          '<div data-value="pm">' + BFHTimePickerModes.pm + '</div>' +
          '</div>';
        modeMax = '11';
      }

      this.$element.html(
        '<div class="' + iconAddon + ' bfh-timepicker-toggle" data-toggle="bfh-timepicker">' +
        iconLeft +
        '<input type="text" name="' + this.options.name + '" class="' + this.options.input + '" placeholder="' + this.options.placeholder + '" readonly>' +
        iconRight +
        '</div>' +
        '<div class="bfh-timepicker-popover">' +
        '<table class="table">' +
        '<tbody>' +
        '<tr>' +
        '<td class="hour">' +
        '<input type="text" class="' + this.options.input + ' bfh-number"  data-min="0" data-max="' + modeMax + '" data-zeros="true" data-wrap="true">' +
        '</td>' +
        '<td class="separator">' + BFHTimePickerDelimiter + '</td>' +
        '<td class="minute">' +
        '<input type="text" class="' + this.options.input + ' bfh-number"  data-min="0" data-max="59" data-zeros="true" data-wrap="true">' +
        '</td>' +
        modeAddon +
        '</tr>' +
        '</tbody>' +
        '</table>' +
        '</div>'
      );
      */
      this.$element
        .on('click.bfhtimepicker.data-api touchstart.bfhtimepicker.data-api', toggle, BFHTimePicker.prototype.toggle)
        .on('click.bfhtimepicker.data-api touchstart.bfhtimepicker.data-api', '.bfh-timepicker-popover > table', function() { return false; });

      this.$element.find('.bfh-number').each(function () {
        var $number;

        $number = $(this);

        $number.bfhnumber($number.data());
        
        $number.on('change', BFHTimePicker.prototype.change);
      });
      
      this.$element.find('.bfh-selectbox').each(function() {
        var $selectbox;

        $selectbox = $(this);

        $selectbox.bfhselectbox($selectbox.data());
        
        $selectbox.on('change.bfhselectbox', BFHTimePicker.prototype.change);
      });
      
      this.setTime();

      this.updatePopover();
    },

    updatePopover: function() {
      var hour,
          minute,
          mode;

      hour = this.$element.data('hour');
      minute = this.$element.data('minute');
      mode = this.$element.data('mode');

      this.$element.find('.hour input[type=text]').val(hour).change();
      this.$element.find('.minute input[type=text]').val(minute).change();
      this.$element.find('.bfh-selectbox').val(mode);
    },
    
    change: function() {
      var $this,
          $parent,
          $timePicker,
          mode;

      $this = $(this);
      $parent = getParent($this);
      
      $timePicker = $parent.data('bfhtimepicker');
      
      if ($timePicker && $timePicker !== 'undefined') {
        mode = '';
        if ($timePicker.options.mode === '12h') {
          mode = ' ' + BFHTimePickerModes[$parent.find('.bfh-selectbox').val()];
        }
        
        $parent.find('.bfh-timepicker-toggle > input[type="text"]').val($parent.find('.hour input[type=text]').val() + BFHTimePickerDelimiter + $parent.find('.minute input[type=text]').val() + mode);

        $parent.trigger('change.bfhtimepicker');
      }

      return false;
    },

    toggle: function(e) {
      var $this,
          $parent,
          isActive;

      $this = $(this);
      $parent = getParent($this);

      if ($parent.is('.disabled') || $parent.attr('disabled') !== undefined) {
        return true;
      }

      isActive = $parent.hasClass('open');

      clearMenus();

      if (!isActive) {
        $parent.trigger(e = $.Event('show.bfhtimepicker'));

        if (e.isDefaultPrevented()) {
          return true;
        }

        $parent
          .toggleClass('open')
          .trigger('shown.bfhtimepicker');

        $this.focus();
      }

      return false;
    }
  };

  function formatTime(hour, minute) {
    hour = String(hour);
    if (hour.length === 1) {
      hour = '0' + hour;
    }

    minute = String(minute);
    if (minute.length === 1) {
      minute = '0' + minute;
    }

    return hour + BFHTimePickerDelimiter + minute;
  }
  
  function clearMenus() {
    var $parent;

    $(toggle).each(function (e) {
      $parent = getParent($(this));

      if (!$parent.hasClass('open')) {
        return true;
      }

      $parent.trigger(e = $.Event('hide.bfhtimepicker'));

      if (e.isDefaultPrevented()) {
        return true;
      }

      $parent
        .removeClass('open')
        .trigger('hidden.bfhtimepicker');
    });
  }

  function getParent($this) {
    return $this.closest('.bfh-timepicker');
  }


  /* TIMEPICKER PLUGIN DEFINITION
   * ========================== */

  var old = $.fn.bfhtimepicker;

  $.fn.bfhtimepicker = function (option) {
    return this.each(function () {
      var $this,
          data,
          options;

      $this = $(this);
      data = $this.data('bfhtimepicker');
      options = typeof option === 'object' && option;
      this.type = 'bfhtimepicker';

      if (!data) {
        $this.data('bfhtimepicker', (data = new BFHTimePicker(this, options)));
      }
      if (typeof option === 'string') {
        data[option].call($this);
      }
    });
  };

  $.fn.bfhtimepicker.Constructor = BFHTimePicker;

  $.fn.bfhtimepicker.defaults = {
    icon: 'glyphicon glyphicon-time',
    align: 'left',
    input: 'form-control',
    placeholder: '',
    name: '',
    time: 'now',
    mode: '24h'
  };


  /* TIMEPICKER NO CONFLICT
   * ========================== */

  $.fn.bfhtimepicker.noConflict = function () {
    $.fn.bfhtimepicker = old;
    return this;
  };


  /* TIMEPICKER VALHOOKS
   * ========================== */

  var origHook;
  if ($.valHooks.div){
    origHook = $.valHooks.div;
  }
  $.valHooks.div = {
    get: function(el) {
      if ($(el).hasClass('bfh-timepicker')) {
        return $(el).find('.bfh-timepicker-toggle > input[type="text"]').val();
      } else if (origHook) {
        return origHook.get(el);
      }
    },
    set: function(el, val) {
      var $timepicker;
      if ($(el).hasClass('bfh-timepicker')) {
        $timepicker = $(el).data('bfhtimepicker');
        $timepicker.options.time = val;
        $timepicker.setTime();
        $timepicker.updatePopover();
      } else if (origHook) {
        return origHook.set(el,val);
      }
    }
  };


  /* TIMEPICKER DATA-API
   * ============== */

  $(document).ready( function () {
    $('div.bfh-timepicker').each(function () {
      var $timepicker;

      $timepicker = $(this);

      $timepicker.bfhtimepicker($timepicker.data());
    });
  });


  /* APPLY TO STANDARD TIMEPICKER ELEMENTS
   * =================================== */

  $(document)
    .on('click.bfhtimepicker.data-api', clearMenus);

}(window.jQuery);