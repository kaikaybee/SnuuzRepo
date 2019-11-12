checkout to the branch with your user story in it 
ex: git checkout eli

then merge to your current sprint
ex: git merge origin/sprint2

now you will have to handle all of the merge conflicts
ex:
>>>>>HEAD
zoop soup
=====
soup zoop
<<<<<origin/sprint2

choose which is correct and add and commit and push like regular
your branch is now working with all the files from sprint2

next checkout to current sprint
ex: git checkout sprint2

finally merge your user story branch
ex: git merge eli

there shouldnt be any conflicts at this point
if there is rollback head to before your merge and see whats wrong
