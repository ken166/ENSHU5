#include <stdio.h>
#define N 10000

int main(void){
  int i,n,nend;
  double xleft,xright;
  double x[N+1],dx;
  double u[N+1],uu[N+1];
  double c,dt,tend;

  xleft=0.0;
  xright=100.0;
  dx = (xright - xleft)/(double)N;

  c=0.5;
  dt=0.5*dx;
  tend = 50.0;
  nend = (int)tend/dt;

  for(i=0;i<N+1;i++){
    x[i]=((double)i)*dx;
  }

  for(i=0;i<N+1;i++){
    u[i]=0.0;
    if(i<=N/2) u[i]=1.0;
  }

  for(n=0;n<=nend;n++){
    uu[0]=u[1];
    uu[N]=u[N-1];
    
    for(i=1;i<N-1;i++){
      uu[i]=u[i]-c*dt/dx*(u[i]-u[i-1]);
    }
    for(i=0;i<N;i++){
      u[i]=uu[i];
    }
  }
  
  for(i=0;i<N+1;i++){
    printf("%10.5f %10.5f\n",x[i],u[i]);
  }
  return 0;
}
