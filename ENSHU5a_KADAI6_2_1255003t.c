#include <stdio.h>
#include <omp.h>
#define N 1000

int main(void){
  int i,j,n,nend;
  double xleft,xright;
  double T[N+1],dx;
  double TT[N+1],x[N+1];
  double c,dt,tend,k;
  
  xleft=0.0;
  xright=100.0;
  dx = (xright - xleft)/(double)N;
  k=1.0;
  c=0.01;
  dt=0.1*dx*dx;
  tend = 500.0;
  nend = (int)tend/dt;
  
  
  
  for(i=0;i<N+1;i++){
    x[i]=((double)i)*dx;
  }
  
  for(i=0;i<N+1;i++){
    T[i]=15.0;
  }
  T[0]=25;
  T[N]=25;
  TT[0]=25;
  TT[N]=25;
  
  for(n=0;n<=nend;n++){
#pragma omp parallel
    {
#pragma omp for
      for(i=2;i<N-1;i++){
  TT[i]=T[i]+dt*(k*((T[i+2]-T[i])/2/dx-(T[i]-T[i-2])/2/dx)/2/dx-c);	
      }
      TT[1]=TT[2];
      TT[N-1]=TT[N-2];
#pragma omp for
      for(j=0;j<N;j++){
	T[j]=TT[j];
      }
    }
  }
  
  
  for(i=0;i<N+1;i++){
    printf("%10.5f %10.5f\n",x[i],T[i]);
  }
  
  return 0;
}
  
