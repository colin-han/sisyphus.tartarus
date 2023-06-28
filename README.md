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

![example](http://www.plantuml.com/plantuml/png/ZL8zQ_Cm4DxrAsxma7Y8uOLtSIXD8MGgTEbkPFjiNIjBGheuc93_NbbHGyOkzftkkUS3wH1OUZvFMgH2r52M0sbzidugIo13V4IdxI80cgPTcgQ6WxqOz7iXQ81DWMQ65uZR8vbn2wpGHEpA4vIzP9xEce764qe-U_oquwWnWfueFMgK0JzMvZoH_2uP0w_p_z3B9K2BW_K8HKOVutmlqFGq275Hf14spjil80F5Mc37OPlTT5BhDqVHZ69sTLL9HxkHMCjsrzcfclzLZXngCbYjkx_-R9ySjbwQJkLV4as23rrbTOy-AGUMttZlitgAxoTmoVQZPOMGeq1ndIEShBb1p0XevdWKlxVNPAHTQYZkdC_YDUHOVm-r1ghX2hBZg5P3dwuNRdiHs3fnYEJnEVm0)
