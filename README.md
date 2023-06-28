# sisyphus.tartarus

一个类似于plantuml的工作流定义语言。

```
: fillForm initDeploy
  by: @owner;

if ($env = staging) then
  : showForm configFeatureToggle
    by: @releaseOwner
    atLatest: 5 days before $releaseDate;
end

while (proj in $projects)
  : callApi http://api.gitlab.com/v2/pipeline/${proj}
    body: { branch: ${branch} }
    auth: bearer #ciToken;
end

parallel (holder in $stakeholders)
  : confirm please confirm the release ${version} for env ${env}?
    by: $holder;
end

: callApi http://api.gitlab.com/v2/pipeline/${proj}
  body: { action: deploy }
  auth: bearer #ciToken;
```

可以生成一个类似于下图的工作流

![example](http://www.plantuml.com/plantuml/png/dL8zR_8m4DxzAsx801c8z4hl4bILIeYfKfVkaPDSackDRJa7227-Uy_6BNJjaZZtyNmyyMPc7VYmDoeTL0rbEP0nEnVsPGbaYRVeZJihWAPfpqrJmyQTB8QrKZJ0ea1xX2UGxP7ik0IUqCfin9adTyeudRC3ZJlKV0ZuxiRHe0mzG0Oqg6TyYyYve_bLCyuS-_-Xr-SPMXnSG2Zoz5RwQuMsfq6fqqG6OU63-m2oKCG3TZmlivfE6_FYISHCpBwkAkrfDH8RtQuwjw-E_ofF7WrPhEBkjpxN9uLjqBQRyYkH9i8VticP7aDYbYm-yVPzPuy1f2XyaWwv4FE2R0UAor5MoDahY4-GPAKap-lphx2A6_QZY3yQlDkxWEvOc6leq--6wrhDxBpQ28bSZoy0)
