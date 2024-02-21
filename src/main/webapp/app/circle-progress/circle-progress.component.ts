import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';

@Component({
  selector: 'jhi-circle-progress',
  standalone: true,
  imports: [],
  templateUrl: './circle-progress.component.html',
  styleUrl: './circle-progress.component.scss',
})
export class CircleProgressComponent implements OnInit, OnChanges {
  @Input() value!: number | null;

  radius = 54;
  circumference = 2 * Math.PI * this.radius;
  dashoffset!: number | null;

  constructor() {
    this.progress(0);
  }

  ngOnInit() {}

  ngOnChanges(changes: SimpleChanges) {
    if (changes['value'].currentValue !== changes['value'].previousValue) {
      this.progress(changes['value'].currentValue);
    }
  }

  private progress(value: number) {
    const progress = value / 100;
    this.dashoffset = this.circumference * (1 - progress);
  }
}
