#include <stdio.h>
#include <omp.h>

#define g 9.8
#define v0 49.0
int main(void){
  int n,nend;
  double y,vy,t,tend,dt;

  dt=0.001;
  tend=10.0;
  nend=tend/dt;
  vy=v0;
  t=0.0;
  y=0.0;
  printf("%10.5f %10.5f \n",t,y);

#pragma omp parallel
  {
#pragma omp for
    for(n=1;n<=nend;n++){
      t=dt*n;
      y=y+vy*dt;
      vy=vy-g*dt;
      printf("%10.5f %10.5f \n",t,y);
    }
  }
  return 0;
}
