#include<stdio.h>
#include<math.h>
#define g 9.8 /*重力加速度*/
#define v0 49 /*初速度*/

int main(void){
  int n,nend;
  double x,vx,y,vy,t,tend,dt,rad,deg;
  dt=0.001;
  tend=7.66;
  nend=tend/dt;
  deg=50.0;
  rad=deg/180.0*M_PI;
  vx=v0*cos(rad);
  vy=v0*sin(rad);
  t=0;
  x=0.0;
  y=0.0;
  printf("%10.5f %10.5f %10.5f \n",t,x,y);  /*初期状態*/

  for(n=1;n<=nend;n++){
    t=dt*n;
    x=x+vx*dt;
    y=y+vy*dt;
    vy=vy-g*dt;
    printf("%10.5f %10.5f %10.5f \n",t,x,y);
  }
  return 0;
}
