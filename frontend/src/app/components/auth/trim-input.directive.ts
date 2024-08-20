import { Directive, ElementRef, HostListener } from '@angular/core';
import { ControlValueAccessor, NgControl } from '@angular/forms';

@Directive({
  selector: '[trimInput]',
  standalone: true,
})
export class TrimInputDirective {
  constructor(private ngControl: NgControl, private el: ElementRef) {
    trimValueAccessor(ngControl.valueAccessor as ControlValueAccessor);
  }

  @HostListener('blur', ['$event.target.value'])
  onBlur() {
    const trimmedValue = this.el.nativeElement.value.trim();
    this.el.nativeElement.value = trimmedValue;
  }
}

// Function to modify the behavior of a value accessor to trim input values
function trimValueAccessor(valueAccessor: ControlValueAccessor) {
  // Store the original registerOnChange function
  const original = valueAccessor.registerOnChange;

  // Override the registerOnChange function to trim input values before calling the original function
  valueAccessor.registerOnChange = (fn: (_: unknown) => void) => {
    return original.call(valueAccessor, (value: unknown) => {
      return fn(typeof value === 'string' ? value.trim() : value);
    });
  };
}
