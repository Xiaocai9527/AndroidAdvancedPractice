e:
cd E:\android\ProjectSpaces\AndroidStudioProjects\AndroidAdvancedPractice
call git status
call git add *
call git commit -m "update %date:~0,4%%date:~5,2%%date:~8,2%%time:~0,2%%time:~3,2%%time:~6,2%"
call git push origin

cmd