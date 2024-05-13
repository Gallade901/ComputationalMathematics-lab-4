import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, LineController, LineElement, PointElement } from 'chart.js';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormsModule, ReactiveFormsModule, FormControl, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';

@Component({
  selector: 'app-choice-mode',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './choice-mode.component.html',
  styleUrl: './choice-mode.component.css'
})
export class ChoiceModeComponent {
  private myChart: ChartJS | null = null;
  pointsForm!: FormGroup;
  countForm!: FormGroup;
  public ans = "";
  public data: any[] = [];
  public data2: any[] = [];
  public numbers: number[] = []//,4,5,6,7,8,9,10,11,12]
  public flag: boolean = false;
  constructor(private http: HttpClient) {
    ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, LineController, LineElement, PointElement);
  }

  onSubmitCount() {
    const numberInputControl = this.countForm.get('numberInput');
    if (numberInputControl) {
      const numberInputValue = numberInputControl.value;
      console.log(numberInputValue)
      for (let i = 0; i < numberInputValue; i++) {
        this.numbers.push(i);
      }
    }
    this.flag = true;
    this.pointsForm = new FormGroup({});
    this.numbers.forEach((num, index) => {
      this.pointsForm.addControl(`x${num}`, new FormControl('', Validators.required));
      this.pointsForm.addControl(`y${num}`, new FormControl('', Validators.required));
    });
  }
  onSubmit() {
   const httpOptions = {
      responseType: 'text' as 'json' // This line tells Angular to expect text response
   };

   this.http.post<any>("http://localhost:8080/app-controller/points", this.pointsForm.value).subscribe(
      data => {
        if (Array.isArray(data)) {
          this.data = data; // Присваиваем полученные данные переменной data
          this.http.get<any>("http://localhost:8080/app-controller/functions").subscribe(
            data2 => {
              if (Array.isArray(data2)) {
                this.data2 = data2;
                this.paint();
              }
            },
            error => {
              console.error('data2=null', error);
            }
          );
        }
      },
      error => {
        console.error('There was an error!', error);
      }
   );
  }

  ngOnInit() {
    this.countForm = new FormGroup({});
    this.countForm.addControl("numberInput", new FormControl('', [Validators.required, Validators.min(8), Validators.max(12)]));

  }
  paint() {
    if (this.myChart) {
      this.myChart.destroy(); // Удаляем существующий график
    }
    let min = Number.MAX_VALUE;
    let max = Number.MIN_VALUE;

    // Перебираем все контролы x
    Object.keys(this.pointsForm.controls).forEach(key => {
      if (key.startsWith('x')) {
        const value = this.pointsForm.get(key)?.value;
        if (value!== undefined && value!== null) {
          min = Math.min(min, value);
          max = Math.max(max, value);
        }
      }
    });
    let rawValues = this.pointsForm.getRawValue();
    let dataP: { x: number; y: number }[] = [];
    let dataX: number[] = [];
    let dataY: number[] = [];
    Object.keys(rawValues).forEach(key => {
      if (key.startsWith('x')) {
        dataX.push(rawValues[key]);
      } else {
        dataY.push(rawValues[key]);
      }
    });
    for (let i = 0; i < dataX.length; i++) {
      let point = {x: dataX[i], y: dataY[i]};
      dataP.push(point);
    }
    dataP.sort((a, b) => a.x - b.x);
    dataX.sort((a, b) => a - b);
    let dataYSort: number[] = [];
    for (let i = 0; i < dataX.length; i++) {
      dataYSort[i] = dataP[i].y;
    }
    const ctx = document.getElementById('myChart') as HTMLCanvasElement;
    this.myChart = new ChartJS(ctx, {
      type: 'line',
      data: {
         labels: [] as number[], // Указываем, что labels будет массивом чисел
        datasets: [
          {
            label: this.data2[0][0] + "x + " + this.data2[0][1],
            data: [] as number[], // Указываем, что data будет массивом чисел
            fill: false,
            borderColor: 'rgb(75, 192, 192)',
            tension: 0.1,
            pointRadius: 0,
            borderWidth: 1
          },
          {
            label: this.data2[1][0] + " + " + this.data2[1][1] + "x + " + this.data2[1][2] + 'x^2',
            data: [] as number[],
            fill: false,
            borderColor: 'rgb(255, 99, 132)', // Измените цвет на желаемый
            tension: 0.1,
            pointRadius: 0,
            borderWidth: 1
          },
          {
            label: this.data2[2][0] + "x^" + this.data2[2][1],
            data: [] as number[],
            fill: false,
            borderColor: 'rgb(54, 162, 25)', // Измените цвет на желаемый
            tension: 0.1,
            pointRadius: 0,
            borderWidth: 1
          },
          {
            label: this.data2[3][0] + "e^" + this.data2[3][1],
            data: [] as number[],
            fill: false,
            borderColor: 'rgb(28, 162, 235)', // Измените цвет на желаемый
            tension: 0.1,
            pointRadius: 0,
            borderWidth: 1
          },
          {
            label: this.data2[4][0] + "ln(x) + " + this.data2[4][1],
            data: [] as number[],
            fill: false,
            borderColor: 'rgb(14, 15, 235)', // Измените цвет на желаемый
            tension: 0.1,
            pointRadius: 0,
            borderWidth: 1
          },
          {
            label: "points",
            data: dataYSort,
            fill: false,
            borderColor: 'rgb(14, 15, 235)', // Измените цвет на желаемый
            tension: 0.1,
            pointRadius: 6,
            showLine: false

          }
        ]
      },
      options: {
        scales: {
          x: {
            type: 'linear',
            position: 'bottom'
          }
        }
      }
    });

    let h = (max - min + 1) / 100

    // Генерация данных для графика
    let xValues: number[] = []; // Явно указываем тип данных как number[]
    let yValues1: number[] = []; // Явно указываем тип данных как number[]
    let yValues2: number[] = []; // Явно указываем тип данных как number[]
    let yValues3: number[] = [];
    let yValues4: number[] = [];
    let yValues5: number[] = [];
    let yValues6: number[] = [];
    for (let x of dataX) { //let x = min - 1; x <= max + 1; x += h // let x of dataX
      xValues.push(x);
      if (this.data2[0][0] !== null) {
        yValues1.push(this.data2[0][0] * x + this.data2[0][1]);
      }
      if (this.data2[1][0] !== null) {
        yValues2.push(this.data2[1][0] + this.data2[1][1] * x + this.data2[1][2] * x * x);
      }
      if (this.data2[2][0] !== null) {
        yValues3.push(this.data2[2][0] * (x ** this.data2[2][1]));
      }
      if (this.data2[3][0] !== null) {
        yValues4.push(this.data2[3][0] * Math.exp(this.data2[3][1] * x));
      }
      if (this.data2[4][0] !== null) {
        yValues5.push(this.data2[4][0] * Math.log(x) + this.data2[4][1]);
      }

    }

    this.myChart.data.labels = xValues;
    this.myChart.data.datasets[0].data = yValues1; // Обновляем данные для первого графика
    this.myChart.data.datasets[1].data = yValues2; // Добавляем данные для второго графика
    this.myChart.data.datasets[2].data = yValues3; // Добавляем данные для третьего графика
    this.myChart.data.datasets[3].data = yValues4;
    this.myChart.data.datasets[4].data = yValues5;
    this.myChart.update();
  }

}
