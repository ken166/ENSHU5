#include <stdio.h>
#include <omp.h>

int main(void){
  double a[400][400],b[400][400],c[400][400];
  int i,j,k;
  for(i=0;i<400;i++)
    for(j=0;j<400;j++){
      a[i][j]=0.01*i-0.01*j;
      b[i][j]=-0.01*i+0.001*j;
    }
  
#pragma omp parallel private(j,k)
  {
  
#pragma omp for schedule(static,25)
    for(i=0;i<400;i++){
      for(j=0;j<400;j++){
	for(k=0;k<400;k++){
	  c[i][j]+=a[i][k]*b[k][j];
	}
      }
    }
  }
  printf("%lf\n",c[399][399]);
  return 0;	  
}
