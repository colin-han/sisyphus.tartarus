# sisyphus.tartarus

一个类似于plantuml的工作流定义语言。

```
: fillForm initDeploy
  by: @owner;

if ($env = "staging") then
  : fillForm configFeatureToggle
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

![example](https://www.plantuml.com/plantuml/svg/fLF1Yjim5BphAmQl3zb0RIZqucstQLdsL0YKt8DiFzklKIGZArb2tN-lPQcjsGHwM9_4OsRUp4ZU34vQTpegCMxCYoOxYl6EYCQD9KLoe6qSfXFmG9rHDTdX3-XZOKrfNF68xzklszqe_gdCkWBuEm0AhDT9qh1IpyOUasIz1cjsJzGhSn50aW3xVNdPxulf67WF0dZj1U06gvJq6P-GJ8jQrcro3zUHnkf2mxq0Rj5kMgYCRhXz9kbEbdQcRHK98BW9tyBJKj6ZecVflab7Wmke3wZbPK19ZR64D7AU9eHFGxhcHW3NvG4l7Il2ghVc1rWZzGUgN0XqnLX6ggHINtgU4tNEzKMUovwpbfsIPLQPOtv-d_VSas9D-Ioyz7zoNV1UahHaSLVnpXn8Bp2bgMCBFr5QgQkkG1eE-1M3pVu5yFgr05CLXrditIF2U0wuX7cDQ-eYeh-eU6dzN2dYoDyyOir8pnEFZVOhuT-8VoYVbq5YZ3JiU8Zk85l9EkxBiYn62ivlb_-MwlzV_7Njid9Ji0Br_CVurWTdUh6PRFe_-3S0)
