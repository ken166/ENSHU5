#include <stdio.h>
#include <math.h>
#include <omp.h>
#define N 1000
double f(double x){
  return pow(x,2)+pow(10,-5)*sin(x)*exp(x);
}

int main(void){
  double dx,x[N+1],xleft,xright,_dx,dfdx[N+1],max_dfdx;
  int i;
  xleft=5.0;
  xright=18.5;
  dx=(xright-xleft)/(double)N;

#pragma omp parallel reduction(max:max_dfdx)
  {
#pragma omp for
    for(i=0; i<=N; i++){
      x[i]=fabs(xleft+(xright-xleft)*(double)i/(double)N);
    }
    dfdx[0]=0;
    dfdx[N]=0;
#pragma omp for
    for(i=1;i<N;i++){
      dfdx[i]=(f(x[i]+dx)-f(x[i]-dx))*0.5/dx;
    }
    
    max_dfdx=dfdx[0];
#pragma omp for
    for(i=1;i<=N;i++){
      if(dfdx[i] > max_dfdx)
	max_dfdx=dfdx[i];
      
      else 
	max_dfdx=max_dfdx;
    }
  }
  printf("%lf\n",max_dfdx);
  return 0;
}
