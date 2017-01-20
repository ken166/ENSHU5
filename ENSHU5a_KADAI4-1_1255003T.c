#include <stdio.h>
#include <math.h>
#include <omp.h>

double f(double x){
  return exp(-pow(x,2));
}

int main(void){
  double sum,dx,x;
  int N,i;
  N=1000000;
  sum=0.0;
  dx=1/(double)N;
#pragma omp parallel reduction(+:sum)
  {
#pragma omp for
    for(i=1;i<=N;i++){
      x=0.5*dx+(double)i*dx;
      sum+=f(x)*dx;
    }
  }
  printf("%lf\n",sum);
  return 0;
}
